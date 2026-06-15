import React, { useState } from 'react';
import { Plus } from 'lucide-react';
import { validateRut } from '@/utils/validator-rut';

interface EmployeeFormProps {
  onCreate: (employee: {rut: string; firstName: string; lastName: string; email: string }) => Promise<void>;
}

export const EmployeeForm: React.FC<EmployeeFormProps> = ({ onCreate }) => {
  const [formData, setFormData] = useState({
    rut: '',
    firstName: '',
    lastName: '',
    email: ''
  });

  const handleRutChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^0-9.\-kK]/g, '');

    if (filteredInput.length <= 12) {
      setFormData({ ...formData, rut: filteredInput });
    }
  };

  const handleFirstNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]/g, '');
    setFormData({ ...formData, firstName: filteredInput });
  };

  const handleLastNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const filteredInput = input.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]/g, '');
    setFormData({ ...formData, lastName: filteredInput });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateRut(formData.rut)) {
      alert("El RUT ingresado no es válido.");
      return; 
    }

    await onCreate({
      rut: formData.rut,
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email
    });

    setFormData({rut: '', firstName: '', lastName: '', email: '' });
  };

  return (
    <section className="mb-12 p-8 bg-white/30 backdrop-blur-md border border-white/70 rounded-3xl shadow-sm shadow-neutral-200/30">
      <h3 className="text-lg font-bold text-neutral-800 mb-6 flex items-center gap-2">
        <Plus size={18} className="text-neutral-600" /> Registrar Empleado
      </h3>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4 lg:grid lg:grid-cols-5">
        <input 
          placeholder="RUT (ej: 12345678-9)" 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full"
          value={formData.rut}
          onChange={handleRutChange}
          required
        />
        <input 
          placeholder="Nombre" 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full"
          value={formData.firstName}
          onChange={handleFirstNameChange}
          required
        />
        <input 
          placeholder="Apellido" 
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full"
          value={formData.lastName}
          onChange={handleLastNameChange}
          required
        />
        <input 
          placeholder="Correo Electrónico" 
          type="email"
          className="bg-white/50 backdrop-blur-sm border border-white/80 p-3 rounded-xl outline-none focus:outline-none focus:border-blue-500/60 focus:bg-white/80 text-neutral-800 placeholder:text-neutral-400 transition-all text-sm shadow-sm w-full"
          value={formData.email}
          onChange={e => setFormData({ ...formData, email: e.target.value })}
          required
        />
        <button type="submit" className="bg-neutral-900/90 hover:bg-neutral-900 text-white p-3 rounded-xl font-bold uppercase tracking-widest text-xs transition-all active:scale-[0.99] shadow-sm hover:shadow-md h-fit mt-auto w-full">
          Registrar
        </button>
      </form>
    </section>
  );
};