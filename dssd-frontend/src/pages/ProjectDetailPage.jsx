import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router';
import { useAuth } from '@/hooks/useAuth';
import Error404 from '@/layout/Error404';
import StateBadget from '@/components/badges/StateBadget';
import { ArrowLeftIcon, PencilIcon } from '@heroicons/react/24/solid';

const ProjectDetailPage = () => {
  const { id } = useParams();
  const { user, hasPermission } = useAuth();

  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [obs, setObs] = useState("");

  const isOngSol = hasPermission("ong_sol");
  const isOngCol = hasPermission("ong_col");
  const isDirectivo = hasPermission("directivo");

  /* -------------------------- Fetch Project -------------------------- */

  useEffect(() => {
    const fetchProject = async () => {
      try {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/proyectos/${id}`);
        if (!res.ok) throw new Error("Not found");
        const data = await res.json();

        // fallbacks
        data.estado = data.estado || "Pendiente";
        data.actividades = Array.isArray(data.actividades) ? data.actividades : [];

        setProject(data);
      } catch (err) {
        setProject(null);
      } finally {
        setLoading(false);
      }
    };

    fetchProject();
  }, [id]);

  if (loading) {
    return (
      <div className="p-6 text-center text-text-secondary">
        Cargando proyecto...
      </div>
    );
  }

  if (!project) {
    return <Error404 />;
  }

  const isOwner = isOngSol && user?.orgId === project.organizacionCreadoraId;

  /* -------------------------- Acciones API -------------------------- */

  const pasarAEjecucion = async () => {
    await fetch(`/api/v1/proyectos/${id}/ejecucion`, { method: "POST" });
    window.location.reload();
  };

  const colaborarActividad = async (idActividad) => {
    await fetch(`/api/v1/actividades/${idActividad}/colaborar`, {
      method: "POST"
    });
    window.location.reload();
  };

  const enviarObservacion = async () => {
    await fetch(`/api/v1/proyectos/${id}/observacion`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ comentario: obs })
    });
    setObs("");
    window.location.reload();
  };

  /* ***************************************************************** */

  return (
    <div className="container mx-auto p-4 sm:p-6">

      {/* Volver */}
      <div className="mb-6">
        <Link
          to="/proyectos"
          className="inline-flex items-center gap-2 text-sm font-medium text-text-secondary hover:text-brand-primary transition-colors"
        >
          <ArrowLeftIcon className="w-4 h-4" />
          Volver a Proyectos
        </Link>
      </div>

      {/* Título */}
      <div className="flex flex-col sm:flex-row justify-between items-start gap-4 mb-4">
        <h1 className="text-2xl sm:text-3xl font-bold text-text-primary">
          {project.nombre}
        </h1>
        <div className="flex items-center gap-4">
          <StateBadget state={project.estado} />

          {isOwner && (
            <button className="flex items-center gap-1.5 text-sm text-brand-primary font-semibold">
              <PencilIcon className="w-4 h-4" /> Editar
            </button>
          )}
        </div>
      </div>

      <p className="text-text-secondary mb-8">{project.descripcion}</p>

      {/* PASAR A EJECUCIÓN */}
      {isOwner && project.estado === "Pendiente" && (
        <button
          onClick={pasarAEjecucion}
          className="mb-6 px-6 py-2 bg-green-600 hover:bg-green-700 text-white font-semibold rounded-md shadow-md"
        >
          Pasar a Ejecución
        </button>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Columna principal */}
        <div className="lg:col-span-2 space-y-8">
          {/* ACTIVIDADES */}
          <div>
            <h2 className="text-xl font-semibold text-text-primary mb-4">
              Actividades
            </h2>

            <div className="space-y-4">
              {project.actividades.length > 0 ? (
                project.actividades.map((act) => {
                  const colaborador = act.colaborador || null;

                  const puedeColaborar =
                    isOngCol && !colaborador; // no tiene colaborador → disponible

                  const esMiColaboracion =
                    isOngCol && colaborador && colaborador.id === user?.orgId;

                  return (
                    <div
                      key={act.id}
                      className="p-4 bg-surface-secondary rounded-lg flex flex-col gap-2 border border-border-primary"
                    >
                      <div className="flex justify-between items-center">
                        <p className="font-medium text-text-primary">
                          {act.nombre}
                        </p>
                        <StateBadget state={act.estado || "Pendiente"} />
                      </div>

                      {/* Info colaborador */}
                      <p className="text-sm text-text-secondary">
                        Colaborador:{" "}
                        <span className="font-medium">
                          {colaborador
                            ? colaborador.nombre
                            : "Sin colaborador"}
                        </span>
                      </p>

                      {/* ONG_COL puede colaborar */}
                      {puedeColaborar && (
                        <button
                          onClick={() => colaborarActividad(act.id)}
                          className="mt-1 px-3 py-1 text-xs bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded"
                        >
                          Colaborar en esta actividad
                        </button>
                      )}

                      {/* Marca si es su colaboración */}
                      {esMiColaboracion && (
                        <p className="text-xs text-green-400 font-semibold">
                          ✔ Tu organización colabora en esta actividad
                        </p>
                      )}
                    </div>
                  );
                })
              ) : (
                <p className="text-text-tertiary">
                  No hay actividades definidas.
                </p>
              )}
            </div>
          </div>
        </div>

        {/* Columna lateral: Observaciones Directivo */}
        <div className="space-y-6">
          {isDirectivo && (
            <div className="p-4 bg-surface-primary border border-border-primary rounded-lg">
              <h3 className="font-semibold text-text-primary mb-2">
                Añadir Observación
              </h3>

              <textarea
                className="w-full p-2 border border-border-secondary rounded-md bg-background-primary text-text-primary"
                rows="3"
                placeholder="Escribe tu comentario..."
                value={obs}
                onChange={(e) => setObs(e.target.value)}
              ></textarea>

              <div className="flex justify-end mt-3">
                <button
                  onClick={enviarObservacion}
                  className="px-4 py-1.5 text-sm font-semibold text-white bg-success-primary hover:bg-success-primary-hover rounded-md"
                >
                  Enviar
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

    </div>
  );
};

export default ProjectDetailPage;