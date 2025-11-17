import React, { useState, useEffect } from 'react';
import { Link } from 'react-router';
import { Bars3Icon } from '@heroicons/react/24/solid';
import { useAuth } from '@/hooks/useAuth';
import { useTranslation } from 'react-i18next';
import MobileSidebar from "./MobileSidebar";
import ProfileDropdown from '@/components/ProfileDropdown';
import { users as mockUsers } from '../utils/mockData';

const Header = () => {
    const { user, isAuth, login } = useAuth();
    const { t } = useTranslation();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const desktopLinks = [
        { name: 'nav.home', link: '/' },
    ];

    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth >= 1024) {
                setIsSidebarOpen(false);
            }
        };
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    return (
        <>
            <header className="sticky top-0 z-30 h-[var(--header-height)] flex justify-between items-center px-4 sm:px-6 bg-surface-primary border-b border-border-primary shadow-sm">
                <Link to="/">
                    <span className="font-bold text-xl text-text-primary">Proyectia</span>
                </Link>

                <nav className="hidden lg:flex items-center gap-6 text-sm font-medium">
                    {/* {desktopLinks.map((link) => (
                        <Link
                            key={link.name}
                            to={link.link}
                            className="text-text-primary/80 hover:text-brand-primary transition-colors duration-200"
                        >
                            {t(link.name)}
                        </Link>
                    ))} */}
                </nav>

                <div className="flex items-center gap-4">
                    {isAuth && (
                        <div className='hidden lg:flex items-center gap-2 border-r border-border-secondary pr-4'>
                            <span className='text-xs text-text-primary/70'>Switch role:</span>
                            {mockUsers.map(mockUser => (
                                <button 
                                    key={mockUser.id}
                                    onClick={() => login({...mockUser, username: mockUser.name})}
                                    className={`px-2 py-1 text-xs font-semibold rounded-md transition-colors ${
                                        user.role === mockUser.role 
                                            ? 'bg-brand-primary text-white' 
                                            : 'text-text-primary/70 hover:bg-surface-secondary'
                                    }`}>
                                    {mockUser.role} 
                                </button>
                            ))}
                        </div>
                    )}

                    <div className="hidden lg:block">
                        {isAuth ? <ProfileDropdown /> : (
                            <Link to="/login" className="text-sm font-medium text-text-primary/80 hover:text-brand-primary transition-colors duration-200">
                                {t('nav.login')}
                            </Link>
                        )}
                    </div>
                    <button onClick={() => setIsSidebarOpen(true)} className="lg:hidden text-text-primary" aria-label="Abrir menú">
                        <Bars3Icon className="w-7 h-7" />
                    </button>
                </div>
            </header>
            <MobileSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
        </>
    );
};

export default Header;