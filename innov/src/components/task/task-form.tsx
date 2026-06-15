import React, { useState, useEffect } from 'react';
import { Plus } from 'lucide-react';
import { type Task } from '@/services/task-service';
import api from '@/api/axios';

interface TaskStatusItem {
  idTaskStatus: number;
  status: string;
}

interface TaskFormProps {
  onCreate: (task: Task) => Promise<void>;
}

export const TaskForm: React.FC<TaskFormProps> = ({ onCreate }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    projectId: '',
    selectedStatusId: ''
  });

  const [statuses, setStatuses] = useState<TaskStatusItem[]>([]);
  const [isValidating, setIsValidating] = useState<boolean>(false);

  useEffect(() => {
    const loadStatuses = async () => {
      try {
        const response = await api.get<TaskStatusItem[]>('/task-status/all');
        if (response.status === 200) {
          setStatuses(response.data);
          if (response.data.length > 0) {
            setFormData(prev => ({ ...prev, selectedStatusId: String(response.data[0].idTaskStatus) }));
          }
        }
      } catch (error) {
        console.error("⚠️ No se pudieron cargar los estados, usando respaldo nominal:", error);
        setStatuses([
          { idTaskStatus: 1, status: 'PENDIENTE' },
          { idTaskStatus: 2, status: 'EN PROCESO' },
          { idTaskStatus: 3, status: 'COMPLETADA' }
        ]);
        setFormData(prev => ({ ...prev, selectedStatusId: '1' }));
      }
    };
    loadStatuses();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsValidating(true);

    const targetProjectId = Number(formData.projectId);
    try {
      await api.get(`/projects/${targetProjectId}`);
      
    } catch (error) {
      console.error("❌ Validación rechazada. El proyecto no existe.");
      alert(`Error de Integridad: El ID de Proyecto [${targetProjectId}] no existe en el sistema. Ingrese un ID válido.`);
      setIsValidating(false);
      return; 
    }

    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();
    const formattedDate = `${day}-${month}-${year}`;

    const currentStatusObject = statuses.find(s => s.idTaskStatus === Number(formData.selectedStatusId));

    const newTask: Task = {
      name: formData.name,
      description: formData.description,
      dateCreated: formattedDate,
      projectId: targetProjectId,
      taskStatus: {
        idTaskStatus: Number(formData.selectedStatusId),
        status: currentStatusObject ? currentStatusObject.status : 'PENDIENTE'
      }
    };

    try {
      await onCreate(newTask);
      setFormData({ name: '', description: '', projectId: '', selectedStatusId: statuses[0]?.idTaskStatus ? String(statuses[0].idTaskStatus) : '' });
    } catch (err) {
      alert("Error al guardar la tarea en ms-tareas");
    } finally {
      setIsValidating(false);
    }
  };

  return (
    <section className="mb-12 p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30">
      <h3 className="text-lg font-bold text-neutral-800 mb-6 flex items-center gap-2">
        <Plus size={18} className="text-neutral-600" /> Crear Nueva Tarea
      </h3>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4 lg:grid lg:grid-cols-4">
        
        {/* Input Nombre */}
        <input 
          placeholder="Nombre de la tarea" 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full lg:col-span-2"
          value={formData.name}
          onChange={e => setFormData({ ...formData, name: e.target.value })}
          required
          disabled={isValidating}
        />

        {/* Input ID Proyecto */}
        <input 
          placeholder="ID de Proyecto Relacionado" 
          type="number"
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full"
          value={formData.projectId}
          onChange={e => setFormData({ ...formData, projectId: e.target.value })}
          required
          disabled={isValidating}
        />

        {/* Selector Dinámico de Estado de la Tarea */}
        <select
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 transition-all text-sm shadow-sm w-full h-[46px]"
          value={formData.selectedStatusId}
          onChange={e => setFormData({ ...formData, selectedStatusId: e.target.value })}
          required
          disabled={isValidating}
        >
          {statuses.map(st => (
            <option key={st.idTaskStatus} value={st.idTaskStatus}>
              {st.status}
            </option>
          ))}
        </select>

        {/* Textarea Descripción */}
        <textarea 
          placeholder="Descripción del entregable..." 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full lg:col-span-3 resize-none h-20"
          value={formData.description}
          onChange={e => setFormData({ ...formData, description: e.target.value })}
          required
          disabled={isValidating}
        />

        {/* Botón */}
        <button 
          type="submit" 
          disabled={isValidating}
          className="bg-neutral-900/90 hover:bg-neutral-900 text-white p-3 rounded-xl font-bold uppercase tracking-widest text-xs transition-all active:scale-[0.99] shadow-sm hover:shadow-md h-[46px] mt-auto w-full disabled:opacity-50"
        >
          {isValidating ? 'Validando...' : 'Crear Tarea'}
        </button>
      </form>
    </section>
  );
};