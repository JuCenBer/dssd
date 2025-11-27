import React from 'react';
import { Link } from 'react-router';
import { ClockIcon, BuildingOffice2Icon } from '@heroicons/react/24/outline';
import StateBadget from '../badges/StateBadget'; // Asumo que este es el nombre correcto

// Componente para mostrar la tarjeta de un proyecto
const ProjectCard = ({ project }) => {
  if (!project) return null;

  return (
    <Link 
      to={`/proyectos/${project.id}`}
      onClick={() => {
        if (project.cargadoPor) {
          localStorage.setItem("projectOwner", JSON.stringify(project.cargadoPor));
        }
      }}
      className="block bg-surface-primary border border-border-primary rounded-lg shadow-sm hover:shadow-md transition-shadow duration-200 p-4"
    >
      <div className="flex justify-between items-start">
        <h3 className="font-semibold text-lg text-text-primary mb-2">{project.nombre}</h3>
        <StateBadget state={project.estado} />
      </div>
      <p className="text-sm text-text-secondary mb-4 line-clamp-2">{project.descripcion}</p>
      <div className="flex justify-between items-center text-xs text-text-tertiary">
        <div className="flex items-center gap-1.5">
          <BuildingOffice2Icon className="w-4 h-4" />
          <span>{project.ubicacion}</span>
        </div>
        <div className="flex items-center gap-1.5">
          <ClockIcon className="w-4 h-4" />
          <span>{project.cargadoPor.nombreOng}</span>
        </div>
      </div>
    </Link>
  );
};

export default ProjectCard;
