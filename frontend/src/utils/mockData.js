
// Este archivo contiene datos de prueba para el desarrollo del frontend.
// Serán reemplazados por llamadas a la API.

export const users = [
  { id: '1', name: 'Ong Solicitante', role: 'ong_sol' },
  { id: '2', name: 'Ong Colaboradora', role: 'ong_col' },
  { id: '3', name: 'Juan Pérez', role: 'directivo' },
];

export const projects = [
  {
    id: 'p1',
    nombre: 'Comedor Infantil "Pequeños Gigantes"',
    descripcion: 'Proyecto para asegurar la alimentación y apoyo escolar a 30 niños en situación de vulnerabilidad.',
    organizacionCreadora: 'ONG "Manos que Ayudan"',
    fecha_inicio: '2025-01-15',
    estado: 'En Revisión', // Posibles estados: 'En Revisión', 'Aprobado', 'Rechazado', 'En Curso', 'Finalizado'
    actividades: [
      { id: 'a1', nombre: 'Servicio de almuerzo diario', estado: 'Planificada' },
      { id: 'a2', nombre: 'Taller de lectura y escritura', estado: 'Planificada' },
    ],
    historialRevision: [
        { revisor: 'Juan Pérez', fecha: '2025-01-17', comentario: 'El presupuesto para alimentos parece insuficiente. Por favor, revisar y ajustar.' }
    ],
    colaboradores: [],
  },
  {
    id: 'p2',
    nombre: 'Refugio para animales "Patitas Felices"',
    descripcion: 'Construcción y mantenimiento de un refugio para 100 perros y gatos sin hogar.',
    organizacionCreadora: 'ONG "Manos que Ayudan"',
    fecha_inicio: '2025-02-20',
    estado: 'Aprobado',
    actividades: [
      { id: 'a3', nombre: 'Campaña de recaudación de fondos', estado: 'En Curso' },
      { id: 'a4', nombre: 'Jornada de adopción', estado: 'Planificada' },
    ],
    historialRevision: [
        { revisor: 'Juan Pérez', fecha: '2025-02-22', comentario: 'Excelente iniciativa. Aprobado.' }
    ],
    colaboradores: [
        { id: 'c1', nombre: 'Fundación "Crecer Juntos"', rol: 'Aporte de insumos' }
    ],
  },
  {
    id: 'p3',
    nombre: 'Programa de Inclusión Digital para Adultos Mayores',
    descripcion: 'Capacitación en herramientas digitales para mejorar la calidad de vida y la comunicación de personas de la tercera edad.',
    organizacionCreadora: 'Otra ONG',
    fecha_inicio: '2025-03-01',
    estado: 'Aprobado',
    actividades: [],
    historialRevision: [],
    colaboradores: [],
  }
];
