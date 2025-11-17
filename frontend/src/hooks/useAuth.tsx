import { createContext, useContext, useCallback, useEffect } from 'react';
import { useLocalStorage } from './useLocalStorage';
import { authEvents } from '../services/authEvents';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    // Almacenamos el ID del usuario logueado
    const [storedAuth, setStoredAuth] = useLocalStorage('auth', { role: '', username: '' });

    // Obtenemos el objeto de usuario completo desde nuestros datos mock
    const isAuth = Boolean(storedAuth);

    const login = useCallback((data) => {
        setStoredAuth({
            role: data.role,
            username: data.username || ''
        })
    }, [setStoredAuth]);

    const logout = useCallback(() => {
        setStoredAuth({ role: "", username: '' });
    }, [setStoredAuth]);

    const hasPermission = (role) => {
        if(!storedAuth) return false;
        return role == storedAuth.role;
    }

    // Escuchar eventos de logout desde otros módulos (ej. API client)
    useEffect(() => {
        const handleLogout = () => {
            logout();
        };

        authEvents.on('logout', handleLogout);
        
        return () => {
            authEvents.off('logout', handleLogout);
        };
    }, [logout]);

    return (
        <AuthContext.Provider
            value={{
                user: storedAuth,
                hasPermission,
                isAuth,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);