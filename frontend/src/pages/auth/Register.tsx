import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { toast } from 'react-hot-toast';
import { useAuthStore } from '../../store/useAuthStore';
import api from '../../api/client';

const registerSchema = z.object({
    name: z.string().min(3, 'Nome deve ter pelo menos 3 caracteres'),
    email: z.string().email('E-mail inválido'),
    password: z.string().min(8, 'A senha deve ter pelo menos 8 caracteres'),
    role: z.enum(['MERCHANT', 'BUYER'], { required_error: 'Selecione o tipo de conta' }),
});

type RegisterForm = z.infer<typeof registerSchema>;

export const Register = () => {
    const navigate = useNavigate();
    const setAuth = useAuthStore((state) => state.setAuth);
    const [isLoading, setIsLoading] = useState(false);

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<RegisterForm>({
        resolver: zodResolver(registerSchema),
        defaultValues: { role: 'MERCHANT' }
    });

    const onSubmit = async (data: RegisterForm) => {
        try {
            setIsLoading(true);
            const response = await api.post('/auth/register', data);
            const { token, name, email, role } = response.data;

            setAuth(token, { name, email, role });
            toast.success('Conta criada com sucesso! Bem-vindo(a) ao AgroNorte!');
            navigate('/dashboard');
        } catch (error: any) {
            toast.error(error.response?.data?.detail || 'Erro ao criar conta');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card" style={{ maxWidth: '480px' }}>
                <div className="auth-logo">
                    <h1>AgroNorte</h1>
                    <p>Junte-se ao maior hub agrícola do Mato Grosso</p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit(onSubmit)}>
                    <div className="form-group">
                        <label className="form-label text-muted">Nome Completo</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="João da Silva"
                            {...register('name')}
                        />
                        {errors.name && <span className="form-error">{errors.name.message}</span>}
                    </div>

                    <div className="form-group">
                        <label className="form-label text-muted">E-mail corporativo</label>
                        <input
                            type="email"
                            className="form-input"
                            placeholder="seu@agro.com.br"
                            {...register('email')}
                        />
                        {errors.email && <span className="form-error">{errors.email.message}</span>}
                    </div>

                    <div className="form-group">
                        <label className="form-label text-muted">Senha de acesso (mínimo 8 caracteres)</label>
                        <input
                            type="password"
                            className="form-input"
                            placeholder="••••••••"
                            {...register('password')}
                        />
                        {errors.password && <span className="form-error">{errors.password.message}</span>}
                    </div>

                    <div className="form-group">
                        <label className="form-label text-muted">Tipo de Conta</label>
                        <select className="form-input" {...register('role')}>
                            <option value="MERCHANT">Comerciante / Revenda</option>
                            <option value="BUYER">Comprador / Produtor Rural</option>
                        </select>
                        {errors.role && <span className="form-error">{errors.role.message}</span>}
                    </div>

                    <button type="submit" className="btn btn-primary btn-full mt-4" disabled={isLoading}>
                        {isLoading ? <div className="spinner" /> : 'Criar Conta'}
                    </button>
                </form>

                <p className="text-center text-sm text-muted mt-4">
                    Já tem uma conta? {' '}
                    <Link to="/login" className="text-primary-400 font-bold" style={{ textDecoration: 'none' }}>
                        Fazer login
                    </Link>
                </p>
            </div>
        </div>
    );
};
