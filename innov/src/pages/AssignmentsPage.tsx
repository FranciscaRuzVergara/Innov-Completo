import { useEffect, useState } from "react";
import api from "../api/axios";
import { AssignmentForm } from "@/components/assignment/assignment-form";
import { AssignmentCard } from "@/components/assignment/assignment-card";

interface Assignment {
  id?: number;
  employeeRut: string;
  taskRoleId: number;
  assignedHours: number;
}

const AssignmentsPage = () => {
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchAssignments = async () => {
    try {
      const response = await api.get("/assignments");
      if (response.status === 200) setAssignments(response.data);
      else if (response.status === 204) setAssignments([]);
    } catch (error: unknown) {
      // Validación nativa de TypeScript
      if (error && typeof error === "object" && "response" in error) {
        const anyError = error as { response?: { status?: number } };
        console.error("Error:", anyError.response?.status);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAssignments();
  }, []);

  const handleCreate = async (payload: {
    employeeRut: string;
    taskRoleId: number;
    assignedHours: number;
  }) => {
    try {
      await api.post("/assignments", payload);
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
      alert("Error do eliminar");
    }
  };

  return (
    <div className="min-h-screen w-full bg-gradient-to-tr from-neutral-100 via-neutral-50 to-blue-50/30 p-4 md:p-8 font-sans text-neutral-800">
      <main className="max-w-6xl mx-auto">
        <AssignmentForm onCreate={handleCreate} />

        {/* Listado */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {!loading &&
            assignments.map((item) => (
              <AssignmentCard
                key={item.id || item.employeeRut}
                item={item}
                onDelete={handleDelete}
              />
            ))}
        </div>
      </main>
    </div>
  );
};

export default AssignmentsPage;
