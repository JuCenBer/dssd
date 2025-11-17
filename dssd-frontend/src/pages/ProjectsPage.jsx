import React, { useMemo, useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router';
import { useAuth } from '@/hooks/useAuth';
import ProjectList from '@/components/projects/ProjectList';
import { DocumentPlusIcon } from '@heroicons/react/24/solid';
import { notify } from "@/services/notificationService";

const ProjectsPage = () => {
  const { isAuth, user } = useAuth();
  const location = useLocation();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);

  // Lógica para determinar el título
  const { title } = useMemo(() => {
    if (!user) return { title: 'Proyectos' };

    switch (user.role) {
      case 'ong_sol':
        return { title: 'Mis Proyectos' };
      case 'ong_col':
        return { title: 'Buscar Proyectos para Colaborar' };
      case 'directivo':
        return { title: 'Proyectos para Revisar' };
      default:
        return { title: 'Proyectos' };
    }
  }, [user, location.pathname]);

  useEffect(() => {
    // Si no hay usuario, no se puede hacer la llamada.
    if (!isAuth) {
        setLoading(false);
        return;
    }

    const fetchProjects = async () => {
        setLoading(true);
        // Construye el endpoint usando la variable de entorno
        const endpoint = import.meta.env.VITE_API_URL + '/api/v1/proyectos';

        try {
            const response = await fetch(endpoint, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                // Intenta leer el cuerpo para obtener el mensaje de error del backend
                const errorData = await response.json().catch(() => ({ message: `Error ${response.status} en la API.` }));
                throw new Error(errorData.message || `Error al obtener proyectos: Código de estado ${response.status}`);
            }

            // El backend devuelve List<Integer> (Case IDs)
            const caseIds = await response.json();
            
            // Mapeamos los Case IDs a objetos de proyecto genéricos.
            // Esto es crucial porque el backend solo devuelve IDs.
            const projectsData = caseIds.map(id => ({
                // Usamos el ID como identificador único
                id: id,
                nombre: `Proyecto Bonita Nro. ${id}`, 
                organizacionCreadora: user.name || 'Desconocida', 
                estado: title.split(' ')[0] || 'En Proceso', // Usamos el título para inferir un estado simple
                descripcion: `Tarea asociada al caso Nro. ${id} en el flujo de Bonita.`,
                // Agrega otros campos requeridos por ProjectList si son necesarios
            }));
            
            setProjects(projectsData);

        } catch (error) {
            console.error("Fallo al obtener proyectos:", error);
            notify({ 
                type: "error", 
                message: error.message || "Hubo un error desconocido al cargar los proyectos." 
            });
            setProjects([]);
        } finally {
            setLoading(false);
        }
    };
    
    fetchProjects();
  }, [isAuth]); 

  // --- Renderizado ---
  if (loading) {
      return (
          <div className="container mx-auto p-4 sm:p-6 text-center">
              <p className="text-lg text-text-secondary">Cargando proyectos...</p>
          </div>
      );
  }

  return (
    <div className="container mx-auto p-4 sm:p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl sm:text-3xl font-bold text-text-primary">{title}</h1>
          {user?.role === 'ONG_SOLICITANTE' && (
            <Link 
              to="/proyectos/crear"
              className="inline-flex items-center gap-2 bg-brand-primary text-white font-semibold px-4 py-2 rounded-lg shadow-sm hover:bg-brand-primary-hover transition-colors"
            >
              <DocumentPlusIcon className="w-5 h-5" />
              <span>Crear Proyecto</span>
            </Link>
          )}
      </div>

    <ProjectList projects={projects} />
  </div>
  );
};

export default ProjectsPage;