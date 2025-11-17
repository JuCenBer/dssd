import React from 'react';
import { useParams, Link } from 'react-router';
import { projects as mockProjects } from '@/utils/mockData';
import { useAuth } from '@/hooks/useAuth';
import Error404 from '@/layout/Error404';
import StateBadget from '@/components/badges/StateBadget';
import { ArrowLeftIcon, PencilIcon } from '@heroicons/react/24/solid';

// Vista de detalle para un solo proyecto
const ProjectDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const project = mockProjects.find(p => p.id === id);

  if (!project) {
    return <Error404 />;
  }

  const isOwner = user?.name === project.organizacionCreadora;

  return (
    <div className="container mx-auto p-4 sm:p-6">
      {/* Encabezado y volver */}
      <div className="mb-6">
        <Link to="/proyectos" className="inline-flex items-center gap-2 text-sm font-medium text-text-secondary hover:text-brand-primary transition-colors">
          <ArrowLeftIcon className="w-4 h-4" />
          Volver a Proyectos
        </Link>
      </div>

      {/* Título y estado */}
      <div className="flex flex-col sm:flex-row justify-between items-start gap-4 mb-4">
        <h1 className="text-2xl sm:text-3xl font-bold text-text-primary">{project.nombre}</h1>
        <div className="flex items-center gap-4">
            <StateBadget state={project.estado} />
            {isOwner && <button className="flex items-center gap-1.5 text-sm text-brand-primary font-semibold"><PencilIcon className='w-4 h-4'/> Editar</button>}
        </div>
      </div>

      <p className="text-text-secondary mb-8">{project.descripcion}</p>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Columna principal de contenido */}
        <div className="lg:col-span-2 space-y-8">
          {/* Actividades */}
          <div>
            <h2 class="text-xl font-semibold text-text-primary mb-4">Actividades</h2>
            <div className="space-y-4">
              {project.actividades.length > 0 ? project.actividades.map(act => (
                <div key={act.id} className="p-4 bg-surface-secondary rounded-lg flex justify-between items-center">
                  <p className="font-medium">{act.nombre}</p>
                  <StateBadget state={act.estado} />
                </div>
              )) : <p className="text-text-tertiary">No hay actividades definidas.</p>}
            </div>
          </div>

          {/* Colaboradores */}
          <div>
            <h2 class="text-xl font-semibold text-text-primary mb-4">Organizaciones Colaboradoras</h2>
            <div className="space-y-4">
              {project.colaboradores.length > 0 ? project.colaboradores.map(colab => (
                <div key={colab.id} className="p-4 bg-surface-secondary rounded-lg">
                  <p className="font-medium">{colab.nombre}</p>
                  <p className="text-sm text-text-secondary">Rol: {colab.rol}</p>
                </div>
              )) : <p className="text-text-tertiary">Este proyecto aún no tiene colaboradores.</p>}
            </div>
          </div>
        </div>

        {/* Columna lateral de revisiones */}
        <div className="space-y-6">
          <h2 class="text-xl font-semibold text-text-primary">Historial de Revisiones</h2>
          <div className="space-y-4">
            {project.historialRevision.length > 0 ? project.historialRevision.map((rev, index) => (
              <div key={index} className="p-4 bg-surface-secondary rounded-lg border-l-4 border-yellow-500">
                <p className="text-sm text-text-secondary italic">"{rev.comentario}"</p>
                <p className="text-right text-xs text-text-tertiary mt-2">- {rev.revisor} ({rev.fecha})</p>
              </div>
            )) : <p className="text-text-tertiary">No hay revisiones para este proyecto.</p>}
          </div>

          {/* Formulario de revisión para Directivos */}
          {user?.role === 'DIRECTIVO' && project.estado === 'En Revisión' && (
            <div className="p-4 bg-surface-primary border border-border-primary rounded-lg">
              <h3 className="font-semibold mb-2">Añadir Revisión</h3>
              <textarea 
                className="w-full p-2 border border-border-secondary rounded-md bg-background-primary text-text-primary" 
                rows="3" 
                placeholder="Escribe tu comentario..."
              ></textarea>
              <div className="flex justify-end gap-2 mt-2">
                <button className="px-4 py-1.5 text-sm font-semibold text-white bg-error-primary hover:bg-error-primary-hover rounded-md">Rechazar</button>
                <button className="px-4 py-1.5 text-sm font-semibold text-white bg-success-primary hover:bg-success-primary-hover rounded-md">Aprobar</button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProjectDetailPage;
