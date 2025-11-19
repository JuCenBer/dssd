/**
 * Datos de prueba (mocks) de proyectos que replican la estructura de la API.
 * @type {Array<Object>}
 */
export const mockProjects = [
    {
        "id": 101,
        "nombre": "Mejora de pozos de agua",
        "descripcion": "Proyecto de potabilización y distribución de agua en comunidades rurales de Córdoba.",
        "ubicacion": "Córdoba, Argentina",
        "estado": "EN_PLANIFICACION",
        "organizacionCreadora": "ONG Agua Pura",
    },
    {
        "id": 102,
        "nombre": "Programa de tutorías escolares",
        "descripcion": "Creación de un programa de tutorías para alumnos de primaria en barrios vulnerables de Buenos Aires.",
        "ubicacion": "Buenos Aires, Argentina",
        "estado": "EN_EJECUCION",
        "organizacionCreadora": "Fundación Saber Más",
    },
    {
        "id": 103,
        "nombre": "Campaña de reciclaje urbano",
        "descripcion": "Instalación de puntos verdes y educación ambiental en el centro de Rosario.",
        "ubicacion": "Rosario, Argentina",
        "estado": "PENDIENTE_APROBACION",
        "organizacionCreadora": "EcoCiudadanos",
    },
    {
        "id": 104,
        "nombre": "Refugio temporal para mascotas",
        "descripcion": "Construcción y equipamiento de un refugio para animales rescatados en Mendoza.",
        "ubicacion": "Mendoza, Argentina",
        "estado": "EN_PLANIFICACION",
        "organizacionCreadora": "Patitas Felices",
    },
];

// Opcionalmente, puedes crear un mock para simular la respuesta del backend actual (solo IDs)
export const mockCaseIds = mockProjects.map(p => p.id);