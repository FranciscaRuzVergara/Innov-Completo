import { useEffect, useState } from 'react';
import { TaskForm } from '@/components/task/task-form';
import { TaskCard } from '@/components/task/task-card';
import { getTasks, createTask, deleteTask, type Task } from '@/services/task-service';

export const TasksPage = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchTasks = async () => {
    try {
      const data = await getTasks();
      setTasks(data);
    } catch (error) {
      console.error("🔥 Error consumiendo ms-tareas:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleCreateTask = async (taskPayload: Task) => {
    try {
      const result = await createTask(taskPayload);
      if (result) fetchTasks();
    } catch (error) {
      alert("Error al registrar la tarea en el microservicio");
    }
  };

  const handleDeleteTask = async (id: number) => {
    if (!window.confirm("¿Desea dar de baja esta tarea del sprint?")) return;
    try {
      await deleteTask(id);
      fetchTasks();
    } catch (error) {
      alert("Error al remover la tarea");
    }
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 p-4 md:p-8 font-sans text-neutral-800">
      <main className="max-w-6xl mx-auto">
        <header className="mb-8">
          <span className="text-[10px] font-bold text-neutral-400 uppercase tracking-widest block mb-1">Módulo Central</span>
          <h2 className="text-2xl font-extrabold text-neutral-800 tracking-tight">Tareas</h2>
        </header>

        <TaskForm onCreate={handleCreateTask} />

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {!loading && tasks.map((item) => (
            <TaskCard 
              key={item.idTask} 
              item={item} 
              onDelete={handleDeleteTask} 
            />
          ))}
        </div>
      </main>
    </div>
  );
};