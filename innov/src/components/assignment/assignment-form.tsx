import React, { useState } from 'react';
import { Plus } from 'lucide-react';
import { validateRut } from '@/utils/validator-rut';

interface AssignmentFormProps {
  onCreate: (assignment: { employeeRut: string; taskRoleId: number; assignedHours: number }) => Promise<void>;
}

export const AssignmentForm: React.FC<AssignmentFormProps> = ({ onCreate }) => {
  const [formData, setFormData] = useState({
    employeeRut: '',
    taskRoleId: '',
    assignedHours: ''
  });

  const handleRutChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^0-9.\-kK]/g, '');

    if (filteredInput.length <= 12) {
      setFormData({ ...formData, employeeRut: filteredInput });
    }
  };

  const handleTaskIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^0-9]/g, '');

    if (filteredInput.length <= 3) {
      setFormData({ ...formData, taskRoleId: filteredInput });
    }
  };

  const handleHoursChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^0-9]/g, '');

    if (filteredInput.length <= 3) {
      setFormData({ ...formData, assignedHours: filteredInput });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateRut(formData.employeeRut)) {
      alert("El RUT ingresado no es válido.");
      return; 
    }

    await onCreate({
      employeeRut: formData.employeeRut,
      taskRoleId: parseInt(formData.taskRoleId),
      assignedHours: parseFloat(formData.assignedHours)
    });
    setFormData({ employeeRut: '', taskRoleId: '', assignedHours: '' });
  };

  return (
    <section className="mb-12 p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30">
      <h3 className="text-lg font-bold text-neutral-800 mb-6 flex items-center gap-2">
        <Plus size={18} className="text-neutral-600" /> Nueva Asignación
      </h3>
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <input 
          placeholder="RUT Empleado" 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm"
          value={formData.employeeRut}
          onChange={handleRutChange}
          required
        />
        <input 
          placeholder="ID Rol Tarea" 
          type="text"
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm"
          value={formData.taskRoleId}
          onChange={handleTaskIdChange}
          required
        />
        <input 
          placeholder="Horas" 
          type="text"
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm"
          value={formData.assignedHours}
          onChange={handleHoursChange}
          required
        />
        <button type="submit" className="bg-neutral-900/90 hover:bg-neutral-900 text-white p-3 rounded-xl font-bold uppercase tracking-widest text-xs transition-all active:scale-[0.99] shadow-sm hover:shadow-md">
          Asignar
        </button>
      </form>
    </section>
  );
};