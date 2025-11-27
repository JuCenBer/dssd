import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router';
import { useAuth } from '@/hooks/useAuth';
import Error404 from '@/layout/Error404';
import StateBadget from '@/components/badges/StateBadget';
import { ArrowLeftIcon, PencilIcon } from '@heroicons/react/24/solid';
import { notificationService } from '@/services/notificationService';

const ProjectDetailPage = () => {
  const { id } = useParams();
  const { user, hasPermission } = useAuth();
  const [descripcionColab, setDescripcionColab] = useState({});
  const [mostrarInput, setMostrarInput] = useState({});

  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [obs, setObs] = useState("");

  const isOngSol = hasPermission("ong_sol");
  const isOngCol = hasPermission("ong_col");
  const isDirectivo = hasPermission("directivo");

  /* -------------------------- Fetch Project -------------------------- */

  useEffect(() => {
    

    fetchProject();
  }, [id]);

  const fetchProject = async () => {
	setLoading(true)
      try {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/proyectos/${id}`, {
          credentials: 'include'
        });
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

  const finalizarProyecto = async () => {
    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/proyectos/${id}/finalizar`, { 
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: 'include',
    });
    console.log(response)
    if(response.ok) {
      setLoading(true)
      setTimeout(() => {
        fetchProject();
        notificationService.success("Se ha finalizado el proyecto!")
      }, 3000);

    }
    
  };

  const colaborarActividad = async (idActividad) => {
    const descripcion = descripcionColab[idActividad];

    if (!descripcion || descripcion.trim() === "") {
      alert("La descripción no puede estar vacía");
      return;
    }

    const response = await fetch(
      `${import.meta.env.VITE_API_URL}/api/v1/proyectos/${id}/pedidos/${idActividad}/compromisos`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ descripcion }),
      }
    );

    if (response.ok) {
      notificationService.success("Has enviado la colaboracion!");
	  setLoading(true)
	  setTimeout(() => {
		  fetchProject();
	  }, 3000);
	  setMostrarInput(false);
    }
    };

  const enviarObservacion = async () => {
    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/proyectos/${id}/observacion`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: 'include',
      body: JSON.stringify({ comentario: obs }),
    });
    console.log(response)
    if(response.ok) {
      setObs("");
      setLoading(true)
      setTimeout(() => {
        fetchProject();
        notificationService.success("Se ha enviado la observacion con exito!")
      }, 3000);

    }
  };

  /* ***************************************************************** */

  const puedeFinalizar = isOwner && project.estado === "EN_EJECUCION";

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

          {/* {isOwner && (
            <button className="flex items-center gap-1.5 text-sm text-brand-primary font-semibold">
              <PencilIcon className="w-4 h-4" /> Editar
            </button>
          )} */}
        </div>
      </div>

      <p className="text-text-secondary mb-8">{project.descripcion}</p>

      {/* PASAR A EJECUCIÓN */}
      {puedeFinalizar && (
        <button
          onClick={finalizarProyecto}
          className="mb-6 px-6 py-2 bg-green-600 hover:bg-green-700 text-white font-semibold rounded-md shadow-md"
        >
          Finalizar Proyecto
        </button>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Columna principal */}
        <div className="lg:col-span-2 space-y-8">
          {/* ACTIVIDADES */}
          <div>
            <h2 className="text-2xl font-semibold text-text-primary mb-4">
              Actividades
            </h2>

            <div className="space-y-4">
              {project.actividades.length > 0 ? (
                project.actividades.map((act) => {
                  const colaboracion = act.colaboracion || null;

                  const puedeColaborar =
                    isOngCol && !colaboracion && act.requiereColaboracion; // no tiene colaborador → disponible

                  const esMiColaboracion =
                    isOngCol && colaboracion && colaboracion.userCompromiso.username === user?.username;

                  return (
                    <div
                      key={act.id}
                      className="p-4 bg-surface-secondary rounded-lg flex flex-col gap-2 border border-border-primary"
                    >
                      <div className="flex justify-between items-center">
                        <p className="font-medium text-xl text-text-primary">
                          {act.nombre}
                        </p>
                        {act.requiereColaboracion ? (
                          <>
                            {!act.colaboracion ? (
                              <p className="bg-yellow-800/50 text-yellow-300 border-yellow-700 px-2.5 py-1 text-xs font-medium rounded-full border">Esperando colaboracion</p>
                            ) : <p className="bg-green-800/50 text-green-300 border-green-700 px-2.5 py-1 text-xs font-medium rounded-full border">Colaborando</p>}
                          </>
                        ) : <p className="bg-gray-700/50 text-gray-300 border-gray-600 px-2.5 py-1 text-xs font-medium rounded-full border">Sin Colaboraciones</p>}
                      </div>

                      <div className='flex justify-between gap-10 items-end'>
                        <div className='flex flex-col gap-2 w-2/4'>
							<p className=' text-text-secondary'>
								Tipo de recurso:{" "}
								{act.recurso}
							</p>

                        
							{colaboracion ? (
								<>
									<p className="text-sm text-text-secondary">
										Colaborador: <span className="font-medium">{colaboracion.userCompromiso.nombreOng} - {colaboracion.userCompromiso.username}</span>
									</p>

									{/* Marca si es su colaboración */}
									{esMiColaboracion && (
										<p className="text-xs text-green-400 font-semibold">
										✔ Tu organización colabora en esta actividad
										</p>
									)}

									{esMiColaboracion || isOwner || isDirectivo ? (
										<div className='p-2 text-sm border-brand-secondary rounded bg-background-secondary flex flex-col gap-2'>
											Descripcion:{" "}
											{colaboracion.descripcion}
										</div>
									) : null}
								
								</>
								) : puedeColaborar ? (
								<>
									{!mostrarInput[act.id] ? (
									<button
										onClick={() =>
										setMostrarInput((prev) => ({ ...prev, [act.id]: true }))
										}
										className="mt-1 px-3 py-1 text-sm bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded"
									>
										Colaborar en esta actividad
									</button>
									) : (
									<div className="flex flex-col gap-2 mt-2">
										<textarea
										type="text"
										placeholder="Descripción de la colaboración"
										className="p-2 min-h-24 border rounded bg-background-primary text-sm text-text-primary"
										value={descripcionColab[act.id] || ""}
										onChange={(e) =>
											setDescripcionColab((prev) => ({
											...prev,
											[act.id]: e.target.value,
											}))
										}
										/>

										<button
										onClick={() => colaborarActividad(act.id)}
										className="px-3 py-1 bg-green-600 hover:bg-green-700 text-white rounded text-sm font-semibold"
										>
										Confirmar colaboración
										</button>
									</div>
									)}
							
								</>
							) : null}



                        </div>


                        <p className='text-sm text-text-secondary ml-auto'>
                          Del{" "}
                          {act.fechaInicio}
                          {" "} al {" "}
                          {act.fechaFin}
                        </p>


                      </div>


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