import React from 'react';

/**
 * Skeleton - componente de esqueleto para estados de carga.
 * Props:
 *  - className: clases Tailwind adicionales
 *  - height: alto (p. ej. "24px" o "h-6")
 *  - width: ancho (p. ej. "100%" o "w-1/2")
 *  - style: estilos inline adicionales
 */
const Skeleton = ({ className = '', height, width, style = {}, ...props }) => {
  // Si height/width vienen como valores CSS (contienen px, %, rem, etc.) los pasamos por style.
  const inlineStyle = {
    ...(height && (typeof height === 'string' && !height.startsWith('h-') ? { height } : {})),
    ...(width && (typeof width === 'string' && !width.startsWith('w-') ? { width } : {})),
    ...style,
  };

  // Default classes: claro/oscuro y animación
  const classes = [
    'rounded-md',
    'animate-pulse',      
    'bg-gray-700', 
    className
  ].join(' ').trim();

  return (
    <div
      role="status"
      aria-busy="true"
      className={classes}
      style={inlineStyle}
      {...props}
    />
  );
};

export default Skeleton;