import React, { useMemo } from 'react';
import { useLocation, Link } from 'react-router';
import { useAuth } from '@/hooks/useAuth';
import { projects as mockProjects } from '@/utils/mockData';
import ProjectList from '@/components/projects/ProjectList';
import { DocumentPlusIcon } from '@heroicons/react/24/solid';

const ProjectsPage = () => {
  const { user } = useAuth();
  const location = useLocation();

  // Lógica para determinar el título y los proyectos a mostrar
  const { title, projects } = useMemo(() => {
    if (!user) return { title: 'Proyectos', projects: [] };

    switch (user.role) {
      case 'ONG_SOLICITANTE':
        return {
          title: 'Mis Proyectos',
          projects: mockProjects.filter(p => p.organizacionCreadora === user.name),
        };
      case 'ONG_COLABORADORA':
        return {
          title: 'Buscar Proyectos para Colaborar',
          // Muestra proyectos aprobados que no son de la propia ONG
          projects: mockProjects.filter(p => p.estado === 'Aprobado' && p.organizacionCreadora !== user.name),
        };
      case 'DIRECTIVO':
        return {
          title: 'Proyectos para Revisar', 
          projects: mockProjects.filter(p => p.estado === 'En Revisión'),
        };
      default:
        return { title: 'Proyectos', projects: [] };
    }
  }, [user, location.pathname]);

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
