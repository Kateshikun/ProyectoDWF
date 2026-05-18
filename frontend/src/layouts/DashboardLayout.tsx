import { Outlet, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Plane, BookOpen, CreditCard, ChartBar as BarChart3, Users, LogOut, Menu, X } from 'lucide-react';
import { useState } from 'react';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: BarChart3 },
  { to: '/vuelos', label: 'Vuelos', icon: Plane },
  { to: '/reservaciones', label: 'Reservaciones', icon: BookOpen },
  { to: '/pagos', label: 'Pagos', icon: CreditCard },
];

const adminItems = [
  { to: '/admin/usuarios', label: 'Usuarios', icon: Users },
];

export default function DashboardLayout() {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
      isActive
        ? 'bg-primary-600 text-white shadow-sm'
        : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
    }`;

  const sidebar = (
    <aside className="w-64 bg-white border-r border-slate-200 h-screen flex flex-col">
      <div className="p-6 border-b border-slate-200">
        <div className="flex items-center gap-2">
          <Plane className="h-7 w-7 text-primary-600" />
          <span className="text-lg font-bold text-slate-900">SkyAdmin</span>
        </div>
      </div>
      <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
        {navItems.map((item) => (
          <NavLink key={item.to} to={item.to} end={item.to === '/dashboard'} className={linkClass} onClick={() => setSidebarOpen(false)}>
            <item.icon className="h-5 w-5" />
            {item.label}
          </NavLink>
        ))}
        {isAdmin && (
          <>
            <div className="pt-4 pb-2">
              <span className="px-4 text-xs font-semibold text-slate-400 uppercase tracking-wider">Admin</span>
            </div>
            {adminItems.map((item) => (
              <NavLink key={item.to} to={item.to} className={linkClass} onClick={() => setSidebarOpen(false)}>
                <item.icon className="h-5 w-5" />
                {item.label}
              </NavLink>
            ))}
          </>
        )}
      </nav>
      <div className="p-4 border-t border-slate-200">
        <div className="flex items-center gap-3 mb-3 px-2">
          <div className="h-8 w-8 rounded-full bg-primary-100 flex items-center justify-center text-primary-700 text-sm font-bold">
            {user?.username?.charAt(0).toUpperCase()}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-slate-900 truncate">{user?.username}</p>
            <p className="text-xs text-slate-500">{user?.rol}</p>
          </div>
        </div>
        <button
          onClick={handleLogout}
          className="flex items-center gap-3 w-full px-4 py-2.5 rounded-lg text-sm font-medium text-slate-600 hover:bg-error-50 hover:text-error-600 transition-colors"
        >
          <LogOut className="h-5 w-5" />
          Cerrar sesion
        </button>
      </div>
    </aside>
  );

  return (
    <div className="flex h-screen bg-slate-50">
      {/* Desktop sidebar */}
      <div className="hidden lg:flex">{sidebar}</div>

      {/* Mobile overlay */}
      {sidebarOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="fixed inset-0 bg-black/40" onClick={() => setSidebarOpen(false)} />
          <div className="fixed inset-y-0 left-0 z-50">{sidebar}</div>
        </div>
      )}

      <div className="flex-1 flex flex-col min-w-0">
        <header className="h-16 bg-white border-b border-slate-200 flex items-center px-4 lg:px-8 gap-4">
          <button
            onClick={() => setSidebarOpen(true)}
            className="lg:hidden p-2 rounded-lg hover:bg-slate-100"
          >
            {sidebarOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </button>
          <h1 className="text-lg font-semibold text-slate-900">Sistema de Boletos Aereos</h1>
        </header>
        <main className="flex-1 overflow-y-auto p-4 lg:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
