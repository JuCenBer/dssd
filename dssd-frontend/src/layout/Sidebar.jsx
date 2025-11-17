import React from 'react';
import { NavLink } from 'react-router';
import { useAuth } from '@/hooks/useAuth';

// Importamos algunos íconos para que quede más prolijo
import { 
    HomeIcon, 
    ClipboardDocumentListIcon, 
    BriefcaseIcon,
    Cog6ToothIcon 
} from '@heroicons/react/24/solid';

// Definimos los links en un array para que el código sea más limpio
const navigationLinks = [
    { name: 'Proyectos', href: '/proyectos', icon: HomeIcon, permission: null }, // Sin permiso, visible para todos
    { name: 'Pedidos de colaboracion', href: '/pedidos', icon: ClipboardDocumentListIcon, permission: 'ong_col' },
    { name: 'Gestión de Proyectos', href: '/gestion-proyectos', icon: BriefcaseIcon, permission: 'directivo' },
];

const Sidebar = () => {
    const { hasPermission } = useAuth();

    // Estilos base para los NavLink, para no repetirlos
    const linkBaseClasses = "flex items-center px-4 py-3 rounded-lg text-sm font-medium transition-colors duration-200";
    const linkInactiveClasses = "text-text-secondary hover:bg-surface-tertiary hover:text-text-primary";
    const linkActiveClasses = "bg-brand-primary text-white shadow-sm";

    return (
        <aside className="hidden lg:flex lg:flex-col w-64 h-full bg-surface-secondary border-r border-border-primary">
            {/* <div className="p-4">
                <h2 className="text-lg font-bold text-text-primary">Navegación</h2>
            </div> */}
            <nav className="my-4 flex-grow px-4 pb-4">
                <ul className="space-y-2">
                    {navigationLinks.map((item) => (
                        // Verificamos si el link requiere un permiso Y si el usuario lo tiene.
                        // Si no requiere permiso (`!item.permission`), se muestra siempre.
                        (!item.permission || hasPermission(item.permission)) && (
                            <li key={item.name}>
                                <NavLink
                                    to={item.href}
                                    end // 'end' asegura que solo la ruta exacta se marque como activa
                                    className={({ isActive }) => 
                                        `${linkBaseClasses} ${isActive ? linkActiveClasses : linkInactiveClasses}`
                                    }
                                >
                                    <item.icon className="h-5 w-5 mr-3" />
                                    <span>{item.name}</span>
                                </NavLink>
                            </li>
                        )
                    ))}
                </ul>
            </nav>
        </aside>
    );
};

export default Sidebar;