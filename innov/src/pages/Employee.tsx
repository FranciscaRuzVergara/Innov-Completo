import { useEffect, useState } from 'react';
import api from '../api/axios';
import { EmployeeForm } from '@/components/employee/employee-form';
import { EmployeeCard } from '@/components/employee/employee-card';
import { GlobalLoading } from '@/components/global/loading';

interface Employee {
  id?: number;
  rut: string;
  firstName: string;
  lastName: string;
  email: string;
}

const EmployeesPage = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchEmployees = async () => {
    try {
      const response = await api.get('/employees');
      if (response.status === 200) setEmployees(response.data);
      else if (response.status === 204) setEmployees([]);
    } catch (error: unknown) {
      if (error && typeof error === 'object' && 'response' in error) {
        const anyError = error as { response?: { status?: number } };
        console.error("Error:", anyError.response?.status);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleCreate = async (payload: { rut: string; firstName: string; lastName: string; email: string }) => {
    try {
      await api.post('/employees', payload);
      fetchEmployees();
    } catch (error: unknown) {
      alert("Error al registrar empleado");
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("¿Eliminar este empleado?")) return;
    try {
      await api.delete(`/employees/${id}`);
      fetchEmployees();
    } catch (error: unknown) {
      alert("Error al eliminar");
    }
  };

  if (loading) {
    return <GlobalLoading message="Cargando nómina de personal..." />;
  }

  return (
    <div className="absolute inset-0 overflow-y-auto bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 p-24 md:p-28 font-sans text-neutral-800">
      
      <div className="max-w-6xl mx-auto w-full pb-12">
        <main className="w-full">
          <EmployeeForm onCreate={handleCreate} />

          {/* Listado de Empleados */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
            {!loading && employees.map((item) => (
              <EmployeeCard 
                key={item.id || item.rut} 
                item={item} 
                onDelete={handleDelete} 
              />
            ))}
          </div>
        </main>
      </div>
    </div>
  );
};

export default EmployeesPage;