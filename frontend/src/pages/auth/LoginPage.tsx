import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Plane } from 'lucide-react';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import { handleApiError } from '../../utils/errors';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    if (!username || !password) {
      setError('Complete todos los campos');
      return;
    }
    setLoading(true);
    try {
      await login(username, password);
      navigate('/dashboard');
    } catch (err) {
      setError(handleApiError(err, 'Error al iniciar sesion'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-lg border border-slate-200 p-8">
      <div className="flex items-center justify-center gap-2 mb-8">
        <Plane className="h-8 w-8 text-primary-600" />
        <h1 className="text-2xl font-bold text-slate-900">SkyAdmin</h1>
      </div>
      <h2 className="text-center text-lg font-semibold text-slate-900 mb-6">Iniciar sesion</h2>
      {error && <div className="mb-4 p-3 rounded-lg bg-error-50 text-error-600 text-sm">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input label="Usuario" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Ingrese su usuario" />
        <Input label="Contrasena" type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Ingrese su contrasena" />
        <Button type="submit" className="w-full" loading={loading}>Iniciar sesion</Button>
      </form>
      <p className="mt-6 text-center text-sm text-slate-500">
        No tiene cuenta?{' '}
        <Link to="/register" className="text-primary-600 font-medium hover:text-primary-700">Registrese aqui</Link>
      </p>
    </div>
  );
}
