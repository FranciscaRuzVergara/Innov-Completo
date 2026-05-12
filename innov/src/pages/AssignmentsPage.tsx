import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import axios from 'axios';
import { ClipboardList, LogOut, Clock, User, Briefcase, Plus, Trash2 } from 'lucide-react';

interface Assignment {
  id?: number;
  employeeRut: string;
  taskRoleId: number;
  assignedHours: number;
}

const AssignmentsPage = () => {
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [loading, setLoading] = useState(true);
  
  // Estados para el formulario de creación
  const [newAssignment, setNewAssignment] = useState({
    employeeRut: '',
    taskRoleId: '',
    assignedHours: ''
  });

  const fetchAssignments = async () => {
    try {
      const response = await api.get('/assignments');
      if (response.status === 200) setAssignments(response.data);
      else if (response.status === 204) setAssignments([]);
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) console.error("Error:", error.response?.status);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAssignments();
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const payload = {
        employeeRut: newAssignment.employeeRut,
        taskRoleId: parseInt(newAssignment.taskRoleId),
        assignedHours: parseFloat(newAssignment.assignedHours)
      };
      await api.post('/assignments', payload);
      setNewAssignment({ employeeRut: '', taskRoleId: '', assignedHours: '' });
      fetchAssignments();
    } catch (error: unknown) {
      alert("Error al crear asignación");
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("¿Eliminar esta asignación?")) return;
    try {
      await api.delete(`/assignments/${id}`);
      fetchAssignments();
    } catch (error: unknown) {
      alert("Error al eliminar");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
  };

  return (
    <div className="min-h-screen w-full bg-slate-950 bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-900/20 via-slate-950 to-black p-4 md:p-8 font-sans text-white">
      
      {/* Navbar */}
      <nav className="max-w-6xl mx-auto flex justify-between items-center mb-12 p-5 bg-white/[0.03] backdrop-blur-xl border border-white/10 rounded-3xl">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-blue-600 rounded-xl shadow-lg shadow-blue-600/30">
            <ClipboardList size={24} />
          </div>
          <div>
            <h1 className="text-xl font-bold">Innovatech</h1>
            <span className="text-[10px] text-blue-400 font-bold uppercase tracking-widest">Gestión de Tareas</span>
          </div>
        </div>
        <button onClick={handleLogout} className="flex items-center gap-2 px-4 py-2 bg-white/5 hover:bg-red-500/20 text-white/60 hover:text-red-400 border border-white/10 rounded-xl transition-all">
          <LogOut size={18} />
          <span className="text-sm font-semibold">Cerrar Sesión</span>
        </button>
      </nav>

      <main className="max-w-6xl mx-auto">
        {/* Formulario de Creación */}
        <section className="mb-12 p-8 bg-white/[0.02] border border-white/10 rounded-[2.5rem]">
          <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
            <Plus size={20} className="text-blue-400" /> Nueva Asignación
          </h3>
          <form onSubmit={handleCreate} className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <input 
              placeholder="RUT Empleado" 
              className="bg-white/5 border border-white/10 p-3 rounded-xl outline-none focus:ring-1 focus:ring-blue-500"
              value={newAssignment.employeeRut}
              onChange={e => setNewAssignment({...newAssignment, employeeRut: e.target.value})}
              required
            />
            <input 
              placeholder="ID Rol Tarea" 
              type="number"
              className="bg-white/5 border border-white/10 p-3 rounded-xl outline-none focus:ring-1 focus:ring-blue-500"
              value={newAssignment.taskRoleId}
              onChange={e => setNewAssignment({...newAssignment, taskRoleId: e.target.value})}
              required
            />
            <input 
              placeholder="Horas" 
              type="number" step="0.5"
              className="bg-white/5 border border-white/10 p-3 rounded-xl outline-none focus:ring-1 focus:ring-blue-500"
              value={newAssignment.assignedHours}
              onChange={e => setNewAssignment({...newAssignment, assignedHours: e.target.value})}
              required
            />
            <button type="submit" className="bg-blue-600 hover:bg-blue-500 p-3 rounded-xl font-bold transition-colors">
              Asignar
            </button>
          </form>
        </section>

        {/* Listado */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {!loading && assignments.map((item) => (
            <div key={item.id} className="group relative p-8 bg-white/[0.04] border border-white/10 rounded-[2.5rem] hover:bg-white/[0.07] transition-all">
              <button 
                onClick={() => item.id && handleDelete(item.id)}
                className="absolute top-6 right-6 p-2 bg-red-500/10 text-red-500 opacity-0 group-hover:opacity-100 rounded-lg hover:bg-red-500 hover:text-white transition-all"
              >
                <Trash2 size={16} />
              </button>

              <div className="absolute top-6 right-16 flex items-center gap-1.5 px-3 py-1 bg-blue-500/10 border border-blue-500/30 rounded-full">
                <Clock size={12} className="text-blue-400" />
                <span className="text-[11px] font-bold text-blue-400">{item.assignedHours} hrs</span>
              </div>

              <div className="space-y-6">
                <div className="flex items-start gap-4">
                  <div className="mt-1 p-2 bg-white/5 rounded-lg"><User size={20} className="text-white/40" /></div>
                  <div>
                    <p className="text-[10px] font-bold text-white/30 uppercase">Empleado (RUT)</p>
                    <p className="text-lg font-semibold">{item.employeeRut}</p>
                  </div>
                </div>
                <div className="flex items-start gap-4">
                  <div className="mt-1 p-2 bg-white/5 rounded-lg"><Briefcase size={20} className="text-white/40" /></div>
                  <div>
                    <p className="text-[10px] font-bold text-white/30 uppercase">ID Rol de Tarea</p>
                    <p className="text-lg font-mono">#{item.taskRoleId}</p>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};

export default AssignmentsPage;