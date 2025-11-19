import React, { useMemo, useEffect, useState, useCallback } from 'react';
import { useLocation, Link } from 'react-router';
import { useAuth } from '@/hooks/useAuth';
import ProjectList from '@/components/projects/ProjectList';
import { DocumentPlusIcon } from '@heroicons/react/24/solid';
import { notify } from "@/services/notificationService";
import Skeleton from '@/components/Skeleton';

const ProjectsPage = () => {
    const { isAuth, user, hasPermission } = useAuth();
    const location = useLocation();
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);

    const { title } = useMemo(() => {
        if (!user) return { title: 'Proyectos' };

        const role = user.role.toUpperCase();
        switch (role) {
            case 'ONG_SOL':
            case 'ONG_SOLICITANTE':
                return { title: 'Mis Proyectos' };
            case 'ONG_COL':
            case 'ONG_COLABORADORA':
                return { title: 'Buscar Proyectos para Colaborar' };
            case 'DIRECTIVO':
                return { title: 'Proyectos para Revisar' };
            default:
                return { title: 'Proyectos' };
        }
    }, [user, location.pathname]);


    const fetchProjectsFromAPI = useCallback(async () => {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/proyectos`, {
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error("No se pudieron obtener los proyectos");
        }

        return await response.json();
    }, []);


    useEffect(() => {
        if (!isAuth) {
            setLoading(false);
            return;
        }

        const load = async () => {
            setLoading(true);
            try {
                await new Promise(res => setTimeout(res, 600));
                const result = await fetchProjectsFromAPI();
                setProjects(result);
            } catch (err) {
                notify({ type: "error", message: err.message });
                setProjects([]);
            } finally {
                setLoading(false);
            }
        };

        load();
    }, [isAuth, user, fetchProjectsFromAPI]);


    const projectItems = projects.map(p => ({
        ...p,
        linkTo: `/proyectos/${p.id}`
    }));


    return (
        <div className="container mx-auto p-4 sm:p-6">

            {/* Título siempre visible */}
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl sm:text-3xl font-bold text-gray">{title}</h1>

                {hasPermission("ong_sol") && (
                    <Link
                        to="crear"
                        className="bg-blue-700 text-white rounded-md py-2 px-3 hover:bg-blue-500 transition-colors"
                    >
                        Crear Proyecto
                    </Link>
                )}

                {user?.role.toUpperCase() === 'ONG_SOLICITANTE' && (
                    <Link 
                        to="/proyectos/crear"
                        className="inline-flex items-center gap-2 bg-indigo-600 text-white font-semibold px-4 py-2 rounded-lg shadow-md hover:bg-indigo-700 transition-colors"
                    >
                        <DocumentPlusIcon className="w-5 h-5" />
                        <span>Crear Proyecto</span>
                    </Link>
                )}
            </div>


            {/* LOADING DENTRO DEL CUERPO DEL COMPONENTE */}
            {loading && (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    {Array.from({ length: 9 }).map((_, i) => (
                        <div 
                            key={i} 
                            className="p-4 border-dark rounded-lg shadow-sm bg-slate-800 flex flex-col gap-3"
                            style={{ minHeight: "150px" }}
                        >
                            <Skeleton height="20px" width="70%" />
                            <Skeleton height="14px" width="90%" />
                            <Skeleton height="14px" width="80%" />
                            <div className="mt-auto">
                                <Skeleton height="32px" width="40%" />
                            </div>
                        </div>
                    ))}
                </div>
            )}


            {/* LISTA REAL DE PROYECTOS */}
            {!loading && (
                <>
                    <ProjectList projects={projectItems} />

                    {projectItems.length === 0 && (
                        <div className="text-center p-10 bg-gray-50 rounded-lg border border-gray-200">
                            <p className="text-xl text-gray-500">No se encontraron proyectos disponibles.</p>
                        </div>
                    )}
                </>
            )}
        </div>
    );
};

export default ProjectsPage;
