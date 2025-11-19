import { Outlet } from "react-router"; 
import Header from "./Header";
import Sidebar from "./Sidebar"; 
import BottomNavBar from "@/layout/BottomNavBar";

const Index = () => {
    return (
        // La estructura general sigue siendo una columna vertical
        <div className="flex flex-col h-screen bg-surface-primary text-text-primary">
            <Header />
            
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar siempre visible en pantallas grandes (lg) */}
                <Sidebar />
                
                {/* Contenido de la página actual */}
                <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto">
                    {/* El overflow-y-auto es clave para que el contenido tenga scroll si es muy largo,
                        y no toda la página. */}
                    <Outlet />
                </main>
            </div>

        </div>
    );
}

export default Index;