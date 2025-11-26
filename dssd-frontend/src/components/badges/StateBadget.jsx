import React from 'react';
import { useTranslation } from 'react-i18next';

const StateBadget = ({ state }) => {
    let className = '';
    let stateText;
    const {t} = useTranslation()

    switch (state) {
        case 'EN_PLANIFICACION':
            className = 'bg-gray-700/50 text-gray-300 border-gray-600';
            stateText = "En Planificacion";
            break;
        case 'EN_EJECUCION':
            className = 'bg-gray-700/50 text-gray-300 border-gray-600';
            stateText =  "En Ejecucion";
            break;
        case 'FINALIZADO':
            className = 'bg-green-800/50 text-green-300 border-green-700';
            stateText = "Finalizado"
            break;
        case 'creating':
            className = 'bg-yellow-800/50 text-yellow-300 border-yellow-700';
            stateText = t('state.creating');
            break;
        default:
            className = 'bg-gray-700/50 text-gray-300 border-gray-600';
            stateText = 'Unknown';
    }

    return (
        <span className={`px-2.5 py-1 text-xs font-medium rounded-full border capitalize ${className}`}>
            {stateText}
        </span>
    );
};

export default StateBadget;