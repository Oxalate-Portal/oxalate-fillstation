import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import Navigation from './components/Navigation';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';
import ResetPasswordPage from './pages/ResetPasswordPage';
import VerifyEmailPage from './pages/VerifyEmailPage';
import DashboardPage from './pages/DashboardPage';
import CylindersPage from './pages/CylindersPage';
import FillsPage from './pages/FillsPage';
import OperatorPage from './pages/OperatorPage';
import AdminPage from './pages/AdminPage';
import NotFoundPage from './pages/NotFoundPage';

const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  { path: '/forgot-password', element: <ForgotPasswordPage /> },
  { path: '/reset-password', element: <ResetPasswordPage /> },
  { path: '/verify-email', element: <VerifyEmailPage /> },
  { path: '/', element: <PrivateRoute><Navigation><DashboardPage /></Navigation></PrivateRoute> },
  { path: '/cylinders', element: <PrivateRoute><Navigation><CylindersPage /></Navigation></PrivateRoute> },
  { path: '/fills', element: <PrivateRoute><Navigation><FillsPage /></Navigation></PrivateRoute> },
  { path: '/operator', element: <PrivateRoute requiredRole="ROLE_OPERATOR"><Navigation><OperatorPage /></Navigation></PrivateRoute> },
  { path: '/admin', element: <PrivateRoute requiredRole="ROLE_ADMIN"><Navigation><AdminPage /></Navigation></PrivateRoute> },
  { path: '*', element: <NotFoundPage /> },
]);

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}

export default App;
