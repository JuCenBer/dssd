import React, { useEffect, useState } from "react";
import { Link } from "react-router";
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import { PieChart } from "react-minimal-pie-chart";

const MetricsPage = () => {
  const [metrics, setMetrics] = useState({
    totalProyectos: 0,
    proyectosEnEjecucion: 0,
    avancePromedio: 0,
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        const res = await fetch(
          `${import.meta.env.VITE_API_URL}/api/v1/metricas`,
          { credentials: "include" }
        );

        if (!res.ok) throw new Error("Error obteniendo métricas");

        const data = await res.json();
        setMetrics(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchMetrics();
  }, []);

  if (loading) {
    return (
      <div className="p-6 text-center text-text-secondary">
        Cargando métricas...
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 sm:p-6">

      {/* Título */}
      <h1 className="text-3xl font-bold text-text-primary mb-8">
        Métricas Generales
      </h1>

      {/* Grid métricas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

        {/* Total proyectos */}
        <div className="p-6 bg-surface-secondary border border-border-primary rounded-xl flex flex-col items-center">
          <p className="text-text-secondary mb-2">Total de Proyectos</p>
          <p className="text-4xl font-bold text-text-primary">
            {metrics.totalProyectos}
          </p>
        </div>

        {/* En ejecución */}
        <div className="p-6 bg-surface-secondary border border-border-primary rounded-xl flex flex-col items-center">
          <p className="text-text-secondary mb-2">En Ejecución</p>
          <p className="text-4xl font-bold text-brand-primary">
            {metrics.proyectosEnEjecucion}
          </p>
        </div>

        {/* Avance promedio */}
        <div className="p-6 bg-surface-secondary border border-border-primary rounded-xl flex flex-col items-center">
          <p className="text-text-secondary mb-4">Avance Promedio</p>

          <PieChart
            data={[
              { title: "Avance", value: metrics.avancePromedio, color: "#22c55e" },
              { title: "Restante", value: 100 - metrics.avancePromedio, color: "#374151" },
            ]}
            lineWidth={25}
            rounded
            style={{ height: "140px" }}
          />

          <p className="text-xl font-semibold mt-2 text-text-primary">
            {metrics.avancePromedio}%
          </p>
        </div>
      </div>
    </div>
  );
};

export default MetricsPage;
