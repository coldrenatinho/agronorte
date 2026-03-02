import { Link, Outlet, useLocation } from 'react-router-dom';
import { useAuthStore } from '../../store/useAuthStore';
import { LayoutDashboard, Package, ShoppingCart, Settings, LogOut } from 'lucide-react';

export const AppLayout = () => {
    const { user, logout } = useAuthStore();
    const location = useLocation();

    const handleLogout = () => {
        logout();
        window.location.href = '/login';
    };

    const navItems = [
        { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
        { path: '/products', label: 'Produtos', icon: Package },
        { path: '/orders', label: 'Pedidos', icon: ShoppingCart },
        { path: '/settings', label: 'Configurações', icon: Settings },
    ];

    return (
        <div className="app-layout">
            <aside className="sidebar">
                <div className="sidebar-logo">
                    <h1>AgroNorte</h1>
                    <span>SaaS Marketplace Integrado</span>
                </div>

                <nav className="sidebar-nav">
                    <div className="nav-section-title">Menu Principal</div>
                    {navItems.map((item) => {
                        const Icon = item.icon;
                        const isActive = location.pathname.startsWith(item.path);
                        return (
                            <Link
                                key={item.path}
                                to={item.path}
                                className={`nav-item ${isActive ? 'active' : ''}`}
                            >
                                <Icon size={18} />
                                {item.label}
                            </Link>
                        );
                    })}
                </nav>

                <div style={{ padding: '16px', borderTop: '1px solid var(--color-border)' }}>
                    <button className="nav-item text-error" onClick={handleLogout}>
                        <LogOut size={18} />
                        Sair do sistema
                    </button>
                </div>
            </aside>

            <main className="main-content">
                <header className="topbar">
                    <div className="text-sm text-muted">Acessando como: <strong>{user?.name}</strong></div>
                    <div className="badge badge-success">{user?.role}</div>
                </header>

                <div className="page-content">
                    <Outlet />
                </div>
            </main>
        </div>
    );
};
