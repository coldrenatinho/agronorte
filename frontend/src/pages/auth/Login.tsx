import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { toast } from 'react-hot-toast';
import { useAuthStore } from '../../store/useAuthStore';
import api from '../../api/client';

const loginSchema = z.object({
    email: z.string().email('E-mail inválido'),
    password: z.string().min(6, 'A senha deve ter pelo menos 6 caracteres'),
});

type LoginForm = z.infer<typeof loginSchema>;

export const Login = () => {
    const navigate = useNavigate();
    const setAuth = useAuthStore((state) => state.setAuth);
    const [isLoading, setIsLoading] = useState(false);

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
    });

    const onSubmit = async (data: LoginForm) => {
        try {
            setIsLoading(true);
            const response = await api.post('/auth/login', data);
            const { token, name, email, role } = response.data;

            setAuth(token, { name, email, role });
            toast.success('Login realizado com sucesso!');
            navigate('/dashboard');
        } catch (error: any) {
            toast.error(error.response?.data?.detail || 'Credenciais inválidas');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <div className="auth-logo">
                    <h1>AgroNorte</h1>
                    <p>Potencializando o agro no Norte do MT</p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit(onSubmit)}>
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
                        <label className="form-label text-muted">Senha de acesso</label>
                        <input
                            type="password"
                            className="form-input"
                            placeholder="••••••••"
                            {...register('password')}
                        />
                        {errors.password && <span className="form-error">{errors.password.message}</span>}
                    </div>

                    <button type="submit" className="btn btn-primary btn-full mt-4" disabled={isLoading}>
                        {isLoading ? <div className="spinner" /> : 'Entrar na Plataforma'}
                    </button>
                </form>

                <div className="auth-divider mt-4">ou</div>

                <p className="text-center text-sm text-muted mt-4">
                    Ainda não é parceiro? {' '}
                    <Link to="/register" className="text-primary-400 font-bold" style={{ textDecoration: 'none' }}>
                        Cadastre sua revenda
                    </Link>
                </p>
            </div>
        </div>
    );
};
