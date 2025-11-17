import React from 'react';
import ProjectCard from './ProjectCard';

// Componente para renderizar una lista de tarjetas de proyecto
const ProjectList = ({ projects }) => {
  if (!projects || projects.length === 0) {
    return (
      <div className="text-center py-10">
        <p className="text-text-secondary">No se encontraron proyectos.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {projects.map(project => (
        <ProjectCard key={project.id} project={project} />
      ))}
    </div>
  );
};

export default ProjectList;
