import React from 'react';
import { Trash2, User, Key, Mail } from 'lucide-react';

interface Employee {
  id?: number;
  rut: string;
  firstName: string;
  lastName: string;   
  email: string;      
}

interface EmployeeCardProps {
  item: Employee;
  onDelete: (id: number) => void;
}

export const EmployeeCard: React.FC<EmployeeCardProps> = ({ item, onDelete }) => {
  return (
    <div className="group relative p-8 bg-white/40 backdrop-blur-md border border-white/70 rounded-3xl hover:bg-white/60 hover:border-white transition-all shadow-sm shadow-neutral-200/50">
      <button 
        onClick={() => item.id && onDelete(item.id)}
        className="absolute top-6 right-6 p-2 bg-red-500/10 text-red-600 opacity-0 group-hover:opacity-100 rounded-xl hover:bg-red-600 hover:text-white transition-all border border-red-200/20"
      >
        <Trash2 size={16} />
      </button>

      <div className="space-y-6">
        {/* Nombre Completo del Empleado */}
        <div className="flex items-start gap-4">
          <div className="mt-1 p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <User size={20} className="text-neutral-500" />
          </div>
          <div>
            <p className="text-[10px] font-bold text-neutral-400 uppercase tracking-wider">Nombre Empleado</p>
            {/* Concatenamos Nombre y Apellido de forma limpia */}
            <p className="text-base font-semibold text-neutral-800">{`${item.firstName} ${item.lastName}`}</p>
          </div>
        </div>

        {/* RUT de Identidad */}
        <div className="flex items-start gap-4">
          <div className="mt-1 p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <Key size={20} className="text-neutral-500" />
          </div>
          <div>
            <p className="text-[10px] font-bold text-neutral-400 uppercase tracking-wider">RUT de Identidad</p>
            <p className="text-base font-mono font-medium text-neutral-700">{item.rut}</p>
          </div>
        </div>

        {/* Correo Electrónico Corporativo */}
        <div className="flex items-start gap-4">
          <div className="mt-1 p-2 bg-white/60 border border-white/80 rounded-xl shadow-sm">
            <Mail size={20} className="text-neutral-500" />
          </div>
          <div>
            <p className="text-[10px] font-bold text-neutral-400 uppercase tracking-wider">Correo Electrónico</p>
            <p className="text-sm font-medium text-neutral-600 break-all">{item.email}</p>
          </div>
        </div>
      </div>
    </div>
  );
};