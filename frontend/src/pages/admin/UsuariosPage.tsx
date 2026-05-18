import { useEffect, useState, type FormEvent } from 'react';
import { adminService } from '../../api/services/admin.service';
import { useApi } from '../../hooks/useApi';
import DataTable, { type Column } from '../../components/shared/DataTable';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Modal from '../../components/ui/Modal';
import Badge from '../../components/ui/Badge';
import ConfirmDialog from '../../components/shared/ConfirmDialog';
import EmptyState from '../../components/ui/EmptyState';
import { Users, Pencil, Trash2, UserCheck, UserX } from 'lucide-react';
import type { UsuarioResponse, UsuarioDTO } from '../../api/types';
import toast from 'react-hot-toast';

export default function UsuariosPage() {
  const { loading, execute } = useApi();
  const [usuarios, setUsuarios] = useState<UsuarioResponse[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showEdit, setShowEdit] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [toggleId, setToggleId] = useState<{ id: number; activo: boolean } | null>(null);
  const [form, setForm] = useState<UsuarioDTO>({ username: '', password: '', email: '', rol: 'CLIENTE', activo: true });

  const loadUsuarios = async () => {
    const data = await execute(() => adminService.listarUsuarios());
    if (data) setUsuarios(data);
  };

  useEffect(() => { loadUsuarios(); }, []);

  const filtered = usuarios.filter((u) => {
    if (!searchTerm) return true;
    const term = searchTerm.toLowerCase();
    return u.username.toLowerCase().includes(term) || u.email.toLowerCase().includes(term);
  });

  const handleEdit = (user: UsuarioResponse) => {
    setEditId(user.id_usuario);
    setForm({ username: user.username, password: '', email: user.email, rol: user.rol, activo: user.estado === 'ACTIVO' });
    setShowEdit(true);
  };

  const handleUpdate = async (e: FormEvent) => {
    e.preventDefault();
    if (!editId) return;
    const ok = await execute(() => adminService.actualizarUsuario(editId, form));
    if (ok !== null) {
      toast.success('Usuario actualizado');
      setShowEdit(false);
      loadUsuarios();
    }
  };

  const handleToggleStatus = async () => {
    if (!toggleId) return;
    const ok = await execute(() => adminService.cambiarEstadoUsuario(toggleId.id, toggleId.activo));
    if (ok !== null) {
      toast.success(toggleId.activo ? 'Usuario activado' : 'Usuario desactivado');
      setToggleId(null);
      loadUsuarios();
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    const ok = await execute(() => adminService.eliminarUsuario(deleteId));
    if (ok !== null) {
      toast.success('Usuario eliminado');
      setDeleteId(null);
      loadUsuarios();
    }
  };

  const statusBadge = (estado: string) => (
    <Badge variant={estado === 'ACTIVO' ? 'success' : 'error'}>{estado}</Badge>
  );

  const columns: Column<UsuarioResponse>[] = [
    { key: 'username', header: 'Usuario' },
    { key: 'email', header: 'Correo' },
    { key: 'rol', header: 'Rol', render: (u) => <Badge variant={u.rol === 'ADMIN' ? 'info' : 'neutral'}>{u.rol}</Badge> },
    { key: 'estado', header: 'Estado', render: (u) => statusBadge(u.estado) },
    {
      key: 'actions',
      header: 'Acciones',
      render: (u) => (
        <div className="flex items-center gap-1">
          <button onClick={() => handleEdit(u)} className="p-1.5 rounded-lg hover:bg-primary-50 text-primary-600 transition-colors" title="Editar">
            <Pencil className="h-4 w-4" />
          </button>
          <button onClick={() => setToggleId({ id: u.id_usuario, activo: u.estado !== 'ACTIVO' })} className="p-1.5 rounded-lg hover:bg-warning-50 text-warning-600 transition-colors" title={u.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}>
            {u.estado === 'ACTIVO' ? <UserX className="h-4 w-4" /> : <UserCheck className="h-4 w-4" />}
          </button>
          <button onClick={() => setDeleteId(u.id_usuario)} className="p-1.5 rounded-lg hover:bg-error-50 text-error-600 transition-colors" title="Eliminar">
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-slate-900">Gestion de Usuarios</h2>

      <Card>
        <div className="flex gap-3 items-end mb-6">
          <Input label="Buscar" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} placeholder="Buscar por nombre o correo" className="w-72" />
        </div>

        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full" /></div>
        ) : filtered.length === 0 ? (
          <EmptyState icon={<Users className="h-12 w-12" />} title="Sin usuarios" description="No se encontraron usuarios" />
        ) : (
          <DataTable columns={columns} data={filtered} keyExtractor={(u) => u.id_usuario} />
        )}
      </Card>

      <Modal open={showEdit} onClose={() => setShowEdit(false)} title="Editar usuario">
        <form onSubmit={handleUpdate} className="space-y-4">
          <p className="text-sm text-slate-500">Editando usuario ID: {editId}</p>
          <Input label="Usuario" value={form.username} onChange={(e) => setForm((p) => ({ ...p, username: e.target.value }))} placeholder="Minimo 4 caracteres" />
          <Input label="Correo" type="email" value={form.email} onChange={(e) => setForm((p) => ({ ...p, email: e.target.value }))} />
          <Input label="Nueva contrasena (dejar vacio para no cambiar)" type="password" value={form.password} onChange={(e) => setForm((p) => ({ ...p, password: e.target.value }))} placeholder="Minimo 8 caracteres" />
          <Select
            label="Rol"
            value={form.rol}
            onChange={(e) => setForm((p) => ({ ...p, rol: e.target.value }))}
            options={[{ value: 'ADMIN', label: 'Administrador' }, { value: 'CLIENTE', label: 'Cliente' }]}
          />
          <div className="flex items-center gap-2">
            <input type="checkbox" id="activo" checked={form.activo} onChange={(e) => setForm((p) => ({ ...p, activo: e.target.checked }))} className="rounded border-slate-300" />
            <label htmlFor="activo" className="text-sm text-slate-700">Activo</label>
          </div>
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowEdit(false)}>Cancelar</Button>
            <Button type="submit" loading={loading}>Guardar</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={!!toggleId}
        onClose={() => setToggleId(null)}
        onConfirm={handleToggleStatus}
        title={toggleId?.activo ? 'Activar usuario' : 'Desactivar usuario'}
        message={`Esta seguro de ${toggleId?.activo ? 'activar' : 'desactivar'} este usuario?`}
        confirmLabel={toggleId?.activo ? 'Activar' : 'Desactivar'}
        loading={loading}
      />

      <ConfirmDialog
        open={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        title="Eliminar usuario"
        message="Esta seguro de eliminar este usuario? Esta accion es permanente."
        confirmLabel="Eliminar"
        loading={loading}
      />
    </div>
  );
}
