import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/api/axios';
import { Save } from 'lucide-react';

const PROJECT_STATUSES = [
  { id: 1, name: 'PLANNING' },
  { id: 2, name: 'IN_PROGRESS' },
  { id: 3, name: 'COMPLETED' }
];

export const ProjectForm: React.FC = () => {
  const navigate = useNavigate();
  const [name, setName] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  
  const [statusId, setStatusId] = useState<number | string>(''); 
  const [taskId, setTaskId] = useState<number | string>(''); 
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const formatDateToBackend = (dateString: string): string => {
    if (!dateString) return '';
    const [year, month, day] = dateString.split('-');
    return `${day}-${month}-${year}`;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!statusId) {
      alert("Por favor, seleccione un estado válido.");
      return;
    }

    setIsSubmitting(true);

    const projectPayload = {
      name: name,
      description: description,
      startDate: formatDateToBackend(startDate),
      endDate: formatDateToBackend(endDate),
      projectStatus: {
        projectStatusId: Number(statusId) 
      },
      kpis: []
    };

    try {
      const projectResponse = await api.post('/projects', projectPayload);
      
      if (projectResponse.status === 201 || projectResponse.status === 200) {
        const creado = projectResponse.data;
        const nuevoProyectoId = creado?.projectId;

        if (taskId && nuevoProyectoId) {
          try {
            const taskResponse = await api.get(`/tasks/${taskId}`);
            
            if (taskResponse.status === 200) {
              const tareaActual = taskResponse.data;

              const taskUpdatePayload = {
                idTask: tareaActual.idTask || Number(taskId),
                name: tareaActual.name,
                description: tareaActual.description,
                dateCreated: tareaActual.dateCreated,
                dateFinished: tareaActual.dateFinished,
                taskStatus: tareaActual.taskStatus, 
                projectId: Number(nuevoProyectoId) 
              };

              await api.put(`/tasks/update/${taskId}`, taskUpdatePayload);
            }
          } catch (taskError) {
            console.error("El proyecto se creó, pero ms-tareas rechazó la actualización en la ruta /update", taskError);
            alert("Proyecto creado, pero no se pudo enlazar la tarea en el microservicio.");
          }
        }

        alert("¡Proyecto creado con éxito!");
        navigate('/projects');
      }
    } catch (error) {
      console.error("Error al crear el proyecto", error);
      alert("Error al procesar la inserción del proyecto. Revise las restricciones.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div>
        <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Nombre del Proyecto</label>
        <input 
          type="text" required value={name} onChange={(e) => setName(e.target.value)}
          className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors"
          placeholder="Ej: Innovatech - Fase 3"
        />
      </div>

      <div>
        <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Descripción</label>
        <textarea 
          required value={description} onChange={(e) => setDescription(e.target.value)}
          className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors h-24 resize-none"
          placeholder="Describa el alcance de los microservicios..."
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Fecha Inicio</label>
          <input 
            type="date" required value={startDate} onChange={(e) => setStartDate(e.target.value)}
            className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors"
          />
        </div>
        <div>
          <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Fecha Término</label>
          <input 
            type="date" required value={endDate} onChange={(e) => setEndDate(e.target.value)}
            className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors"
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Estado Inicial</label>
          <select 
            value={statusId} 
            onChange={(e) => setStatusId(e.target.value)}
            required
            className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors appearance-none"
          >
            <option value="" disabled hidden>Selecciona estado</option>
            {PROJECT_STATUSES.map((status) => (
              <option key={status.id} value={status.id}>
                {status.name}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-xs font-bold text-neutral-700 uppercase tracking-wider mb-2">Asociar ID Tarea Existente</label>
          <input 
            type="number" 
            min={1} 
            value={taskId} 
            onChange={(e) => setTaskId(e.target.value)}
            placeholder="Ej: 1" 
            className="w-full px-4 py-3 bg-white border border-neutral-200 rounded-xl text-sm focus:outline-none focus:border-blue-500 transition-colors"
          />
        </div>
      </div>

      <button
        type="submit" disabled={isSubmitting}
        className="w-full inline-flex items-center justify-center gap-2 px-6 py-3.5 bg-neutral-900 text-white rounded-xl text-xs font-bold uppercase tracking-wider hover:bg-blue-600 transition-all disabled:opacity-50"
      >
        <Save size={14} /> {isSubmitting ? 'Guardando...' : 'Confirmar Registro'}
      </button>
    </form>
  );
};