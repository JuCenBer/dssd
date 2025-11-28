import React, { useRef, useState } from 'react';
import SmartForm from '@/components/SmartForm';
import { notify } from '@/services/notificationService';
import { TextInput, TextAreaInput, SelectInput } from '@/components/Inputs';
import {useNavigate} from "react-router"

/* ---------------------------- Input Aux Components ---------------------------- */

const DateInput = ({ label, name, value, onChange, error }) => {
    const inputRef = useRef(null);

    // Mismas clases base que tu TextInput para consistencia
    const baseClasses = "block w-full rounded-md py-2 pl-3 pr-10 shadow-sm transition-colors duration-200 bg-surface-secondary text-text-primary placeholder:text-text-tertiary focus:outline-none cursor-pointer";
    
    // Clases condicionales de borde
    const borderClasses = error
        ? "border-error-primary focus:border-error-primary focus:ring-2 focus:ring-error-primary/50"
        : "border-border-secondary focus:border-border-focus focus:ring-2 focus:ring-border-focus/50";

    // Función para abrir el calendario al hacer click en cualquier parte del input
    const handleDivClick = () => {
        if (inputRef.current) {
            inputRef.current.showPicker();
        }
    };

    return (
        <div className="w-full">
            {label && (
                <label 
                    onClick={handleDivClick} 
                    className="block text-sm font-medium text-text-primary mb-1 cursor-pointer"
                >
                    {label}
                </label>
            )}
            <div className="relative mt-1" onClick={handleDivClick}>
                <input
                    ref={inputRef}
                    type="date"
                    name={name}
                    id={name}
                    value={value || ""}
                    onChange={onChange}
                    className={`border ${baseClasses} ${borderClasses} [color-scheme:dark] [&::-webkit-calendar-picker-indicator]:opacity-0`}
                    // [color-scheme:dark] -> Hace que el popup del calendario sea oscuro en Chrome/Edge
                    // [&::-webkit...]:opacity-0 -> Oculta el icono nativo negro para usar el nuestro
                />
                
                {/* Icono de Calendario Personalizado (Blanco/Gris) */}
                <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
                    <svg 
                        xmlns="http://www.w3.org/2000/svg" 
                        fill="none" 
                        viewBox="0 0 24 24" 
                        strokeWidth={1.5} 
                        stroke="currentColor" 
                        className="w-5 h-5 text-gray-400" // Ajusta este color si tienes una variable de texto específica
                    >
                        <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5" />
                    </svg>
                </div>
            </div>
            {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
        </div>
    );
};

const CheckboxInput = ({ label, name, checked, onChange, error }) => (
    <div className="flex items-center gap-2">
        <input
            type="checkbox"
            name={name}
            id={name}
            checked={checked}
            onChange={onChange}
            className="h-4 w-4 rounded border-gray-500 text-indigo-400 focus:ring-indigo-400"
        />
        <label htmlFor={name} className="text-sm font-medium text-gray-300">
            {label}
        </label>
        {error && <p className="text-red-400 text-sm">{error}</p>}
    </div>
);

/* ------------------------------ Summary Modal ------------------------------- */

const ProjectSummary = ({ projectData, onClose }) => {
    const recursoLabels = {
        'DINERO': 'Dinero',
        'MANO_DE_OBRA': 'Mano de obra',
        'MATERIAL': 'Material',
        'OTRO': 'Otro'
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-50">
            <div className="bg-gray-900 rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto border border-gray-700">
                <div className="p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h2 className="text-2xl font-bold text-green-400">✓ Proyecto Creado</h2>
                        <button
                            onClick={onClose}
                            className="text-gray-400 hover:text-gray-200 text-3xl leading-none"
                        >
                            ×
                        </button>
                    </div>

                    <div className="space-y-6">
                        <div className="bg-blue-900/30 p-4 rounded-lg border border-blue-700/40">
                            <h3 className="font-semibold text-lg text-blue-300 mb-2">{projectData.nombre}</h3>
                            <p className="text-gray-300 mb-2">{projectData.descripcion}</p>
                            <p className="text-sm text-gray-400">
                                <span className="font-medium">Ubicación:</span> {projectData.ubicacion}
                            </p>
                        </div>

                        <div>
                            <h4 className="font-semibold text-lg text-gray-200 mb-3">
                                Actividades ({projectData.actividades?.length || 0})
                            </h4>

                            <div className="space-y-3">
                                {projectData.actividades?.map((activity, index) => (
                                    <div key={index} className="bg-gray-800 p-4 rounded-lg border border-gray-700">
                                        <div className="flex justify-between items-start mb-2">
                                            <h5 className="font-medium text-gray-100">{activity.nombre}</h5>

                                            <span className="text-xs bg-indigo-900 text-indigo-300 px-2 py-1 rounded border border-indigo-700">
                                                {recursoLabels[activity.recurso] || activity.recurso}
                                            </span>
                                        </div>

                                        <div className="grid grid-cols-2 gap-2 text-sm text-gray-300 mb-2">
                                            <div>
                                                <span className="font-medium">Inicio:</span> {activity.fechaInicio}
                                            </div>
                                            <div>
                                                <span className="font-medium">Fin:</span> {activity.fechaFin}
                                            </div>
                                        </div>

                                        {activity.requiereColaboracion && (
                                            <span className="inline-block text-xs bg-yellow-900 text-yellow-300 px-2 py-1 rounded border border-yellow-700">
                                                Requiere colaboración
                                            </span>
                                        )}
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                    <div className="mt-6 flex justify-end">
                        <button
                            onClick={onClose}
                            className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded transition-colors"
                        >
                            Cerrar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

/* ------------------------------ Main Component ------------------------------- */

const CreateProject = () => {
    const [createdProject, setCreatedProject] = useState(null);
    const [showSummary, setShowSummary] = useState(false);
    const navigate = useNavigate()

    const VALID_RECURSOS = ['DINERO', 'MANO_DE_OBRA', 'MATERIAL', 'OTRO'];

    /* ---------------------------- Form Validations ---------------------------- */

    const validations = {
        nombre: {
            function: (value) => value?.trim() !== '',
            message: 'El nombre es obligatorio.'
        },
        descripcion: {
            function: (value) => value?.trim() !== '' && value.length < 255,
            message: 'La descripción es obligatoria y no puede superar los 255 caracteres.'
        },
        ubicacion: {
            function: (value) => value?.trim() !== '',
            message: 'La ubicación es obligatoria.'
        },
        actividades: {
            function: (activities) => {
                if (!Array.isArray(activities) || activities.length === 0) return false;

                return activities.every(act => {
                    if (!act.nombre?.trim() || !act.fechaInicio?.trim() || !act.fechaFin?.trim()) return false;
                    if (!VALID_RECURSOS.includes(act.recurso)) return false;

                    const ini = new Date(act.fechaInicio);
                    const fin = new Date(act.fechaFin);
                    if (isNaN(ini) || isNaN(fin) || fin < ini) return false;

                    return true;
                });
            },
            message: 'Las actividades deben tener todos los campos completos y fechas válidas.'
        }
    };

    /* ------------------------------ Form Success ------------------------------ */

    const handleSuccess = (data) => {
        notify({ type: 'success', message: '¡Proyecto creado con éxito!' });
        navigate("/proyectos")
    };

    const handleCloseSummary = () => {
        setShowSummary(false);
        setCreatedProject(null);
    };

    return (
        <div className="min-h-screen bg-gray-950 text-gray-100 p-4 md:p-8">
            <div className="max-w-4xl mx-auto bg-gray-900 border border-gray-800 rounded-lg shadow-xl p-6">
                <h2 className="text-2xl font-bold mb-6 text-center text-gray-100">Crear Nuevo Proyecto</h2>

                <SmartForm
                    url={`/api/v1/proyectos`}
                    submitText="Crear Proyecto"
                    data={{
                        nombre: '',
                        descripcion: '',
                        ubicacion: '',
                        actividades: []
                    }}
                    validations={validations}
                    onSuccess={handleSuccess}
                    expectEmptyResponse={true}
                >
                    {({ formData, handleChange, errors, saveFormData }) => {
                        const handleActivityChange = (index, field, value) => {
                            const newActivities = [...(formData.actividades || [])];
                            newActivities[index] = { ...newActivities[index], [field]: value };
                            saveFormData({ name: 'actividades', value: newActivities });
                        };

                        const addActivity = () => {
                            const newActivity = {
                                nombre: '',
                                fechaInicio: '',
                                fechaFin: '',
                                recurso: '',
                                requiereColaboracion: false
                            };
                            saveFormData({
                                name: 'actividades',
                                value: [...(formData.actividades || []), newActivity]
                            });
                        };

                        const removeActivity = (index) => {
                            const newActivities = [...(formData.actividades || [])];
                            newActivities.splice(index, 1);
                            saveFormData({ name: 'actividades', value: newActivities });
                        };

                        return (
                            <>
                                <TextInput
                                    name="nombre"
                                    label="Nombre del Proyecto"
                                    value={formData.nombre || ''}
                                    onChange={handleChange}
                                    error={errors.nombre}
                                />

                                <TextAreaInput
                                    name="descripcion"
                                    label="Descripción"
                                    value={formData.descripcion || ''}
                                    onChange={handleChange}
                                    error={errors.descripcion}
                                    rows={4}
                                />

                                <TextInput
                                    name="ubicacion"
                                    label="Ubicación"
                                    value={formData.ubicacion || ''}
                                    onChange={handleChange}
                                    error={errors.ubicacion}
                                />

                                {/* ---------------------------- ACTIVIDADES ---------------------------- */}

                                <div className="mt-6">
                                    <h3 className="text-lg font-semibold mb-2">Actividades</h3>
                                    {errors.actividades && (
                                        <p className="text-red-400 text-sm mb-2">{errors.actividades}</p>
                                    )}

                                    <div className="space-y-4">
                                        {(formData.actividades || []).map((activity, index) => (
                                            <div
                                                key={index}
                                                className="p-4 border border-gray-700 bg-gray-800 rounded-lg relative"
                                            >
                                                <button
                                                    type="button"
                                                    onClick={() => removeActivity(index)}
                                                    className="absolute top-2 right-2 text-red-400 hover:text-red-300 text-2xl leading-none"
                                                >
                                                    &times;
                                                </button>

                                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                                    <TextInput
                                                        name={`actividad_nombre_${index}`}
                                                        label="Nombre Actividad"
                                                        value={activity.nombre}
                                                        onChange={(e) => handleActivityChange(index, 'nombre', e.target.value)}
                                                    />

                                                    <SelectInput
                                                        name={`actividad_recurso_${index}`}
                                                        label="Recurso"
                                                        value={activity.recurso}
                                                        onChange={(e) => handleActivityChange(index, 'recurso', e.target.value)}
                                                        hasNull={false}
                                                    >
                                                        <option value="">Seleccionar recurso</option>
                                                        <option value="DINERO">Dinero</option>
                                                        <option value="MANO_DE_OBRA">Mano de obra</option>
                                                        <option value="MATERIAL">Material</option>
                                                        <option value="OTRO">Otro</option>
                                                    </SelectInput>

                                                    <DateInput
                                                        name={`actividad_fechaInicio_${index}`}
                                                        label="Fecha de Inicio"
                                                        value={activity.fechaInicio}
                                                        onChange={(e) => handleActivityChange(index, 'fechaInicio', e.target.value)}
                                                    />

                                                    <DateInput
                                                        name={`actividad_fechaFin_${index}`}
                                                        label="Fecha de Fin"
                                                        value={activity.fechaFin}
                                                        onChange={(e) => handleActivityChange(index, 'fechaFin', e.target.value)}
                                                    />

                                                    <div className="md:col-span-2 flex items-center">
                                                        <CheckboxInput
                                                            name={`actividad_requiereColaboracion_${index}`}
                                                            label="Requiere Colaboración"
                                                            checked={activity.requiereColaboracion}
                                                            onChange={(e) => handleActivityChange(index, 'requiereColaboracion', e.target.checked)}
                                                        />
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>

                                    <button
                                        type="button"
                                        onClick={addActivity}
                                        className="mt-4 bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition-colors"
                                    >
                                        + Añadir Actividad
                                    </button>
                                </div>
                            </>
                        );
                    }}
                </SmartForm>
            </div>

            {showSummary && createdProject && (
                <ProjectSummary projectData={createdProject} onClose={handleCloseSummary} />
            )}
        </div>
    );
};

export default CreateProject;