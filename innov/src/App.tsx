import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/pages/LoginPage';
import AssignmentsPage from '@/pages/AssignmentsPage';
import EmployeesPage from '@/pages/Employee';
import { ProjectsPage } from '@/pages/ProjectsPage';
import { ProjectDashboardPage } from '@/pages/ProjectDashboardPage';
import { RegisterPage } from '@/pages/RegisterPage';
import { Navbar } from '@/components/global/navbar';
import { TasksPage } from '@/pages/TaskPage';

function App() {
  return (
    <BrowserRouter>
      <Navbar />      
      <Routes>
        <Route path="/login" element={<LoginPage />} />       
        <Route path="/register" element={<RegisterPage />} /> 
        <Route path="/tasks" element={<TasksPage />} />       
        <Route path="/assignments" element={<AssignmentsPage />} />
        <Route path="/employees" element={<EmployeesPage />} />
        <Route path="/projects" element={<ProjectsPage />} />
        <Route path="/projects/:id/dashboard" element={<ProjectDashboardPage />} />
        <Route path="/*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;