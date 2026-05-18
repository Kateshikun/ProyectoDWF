import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Plane } from 'lucide-react';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Button from '../../components/ui/Button';
import { handleApiError } from '../../utils/errors';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '', email: '', rol: 'CLIENTE' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const update = (field: string, value: string) => setForm((prev) => ({ ...prev, [field]: value }));

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    if (!form.username || !form.password || !form.email) {
      setError('Complete todos los campos');
      return;
    }
    if (form.username.length < 4) {
      setError('El usuario debe tener al menos 4 caracteres');
      return;
    }
    if (form.password.length < 8) {
      setError('La contrasena debe tener al menos 8 caracteres');
      return;
    }
    setLoading(true);
    try {
      await register(form);
      navigate('/dashboard');
    } catch (err) {
      setError(handleApiError(err, 'Error al registrar'));
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
      <h2 className="text-center text-lg font-semibold text-slate-900 mb-6">Crear cuenta</h2>
      {error && <div className="mb-4 p-3 rounded-lg bg-error-50 text-error-600 text-sm">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input label="Usuario" value={form.username} onChange={(e) => update('username', e.target.value)} placeholder="Minimo 4 caracteres" />
        <Input label="Correo electronico" type="email" value={form.email} onChange={(e) => update('email', e.target.value)} placeholder="correo@ejemplo.com" />
        <Input label="Contrasena" type="password" value={form.password} onChange={(e) => update('password', e.target.value)} placeholder="Minimo 8 caracteres" />
        <Select
          label="Rol"
          value={form.rol}
          onChange={(e) => update('rol', e.target.value)}
          options={[
            { value: 'CLIENTE', label: 'Cliente' },
            { value: 'ADMIN', label: 'Administrador' },
          ]}
        />
        <Button type="submit" className="w-full" loading={loading}>Crear cuenta</Button>
      </form>
      <p className="mt-6 text-center text-sm text-slate-500">
        Ya tiene cuenta?{' '}
        <Link to="/login" className="text-primary-600 font-medium hover:text-primary-700">Inicie sesion</Link>
      </p>
    </div>
  );
}
