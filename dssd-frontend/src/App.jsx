import './i18n';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router';

// Hooks y Providers
import { ThemeProvider } from './hooks/useTheme';
import { AuthProvider } from './hooks/useAuth';

// Layouts y Rutas Protegidas
import Index from './layout/Index';
import Error404 from './layout/Error404';
import ProtectedRoute from './components/ProtectedRoute';

// Páginas
import Login from './pages/Login';
import ProjectsPage from './pages/ProjectsPage';
import CreateProject from './pages/CreateProject';
import ProjectDetailPage from './pages/ProjectDetailPage';
import Form from './pages/Form'; // Página para crear proyecto

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            {/* Ruta pública para el login */}
            <Route path="/login" element={<Login />} />

            {/* --- RUTAS PROTEGIDAS DENTRO DEL LAYOUT PRINCIPAL --- */}
            <Route 
              path="/" 
              element={
                <ProtectedRoute useLayout={true} /> // Outlet se renderiza en Index
              }
            >
              <Route element={<Index />}>
                {/* Redirige la ruta raíz a la página de proyectos */}
                <Route index element={<Navigate to="proyectos" replace />} />
                
                {/* Rutas que activan el mismo componente, pero el componente decide qué mostrar */}
                <Route path="proyectos" element={<ProjectsPage />} />
                <Route path="proyectos/buscar" element={<ProjectsPage />} />
                <Route path="revisiones" element={<ProjectsPage />} />

                {/* Ruta para crear un nuevo proyecto */}
                <Route path="proyectos/crear" element={<CreateProject />} />

                {/* Ruta para ver el detalle de un proyecto */}
                <Route path="proyectos/:id" element={<ProjectDetailPage />} />
              </Route>
            </Route>

            {/* --- RUTA PARA PÁGINAS NO ENCONTRADAS --- */}
            <Route path="*" element={<Error404 />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
