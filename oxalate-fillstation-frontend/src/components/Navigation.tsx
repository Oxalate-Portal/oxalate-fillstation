import React, {useState} from 'react';
import {Button, Layout, Menu, message, theme} from 'antd';
import {
    DashboardOutlined,
    ExperimentOutlined,
    FileTextOutlined,
    LogoutOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    SettingOutlined,
    TeamOutlined,
} from '@ant-design/icons';
import {useLocation, useNavigate} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {useAuth} from '../context/AuthContext';
import buildInfo from '../buildInfo.json';

const {Sider, Content, Header, Footer} = Layout;

interface NavigationProps {
  children: React.ReactNode;
}

const Navigation: React.FC<NavigationProps> = ({ children }) => {
  const { t } = useTranslation();
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [collapsed, setCollapsed] = useState(false);
  const { token } = theme.useToken();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login');
    } catch {
      message.error(t('common.error'));
    }
  };

  const menuItems = [
    {
      key: '/',
      icon: <DashboardOutlined />,
      label: t('nav.dashboard'),
    },
    {
      key: '/cylinders',
      icon: <ExperimentOutlined />,
      label: t('nav.cylinders'),
    },
    {
      key: '/fills',
      icon: <FileTextOutlined />,
      label: t('nav.fills'),
    },
    ...(user?.roles.includes('ROLE_OPERATOR') || user?.roles.includes('ROLE_ADMIN')
      ? [
          {
            key: '/operator',
            icon: <TeamOutlined />,
            label: t('nav.operator'),
          },
        ]
      : []),
    ...(user?.roles.includes('ROLE_ADMIN')
      ? [
          {
            key: '/admin',
            icon: <SettingOutlined />,
            label: t('nav.admin'),
          },
        ]
      : []),
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed} trigger={null}>
        <div
          style={{
            height: 32,
            margin: 16,
            background: 'rgba(255,255,255,0.2)',
            borderRadius: 6,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#fff',
            fontWeight: 'bold',
            overflow: 'hidden',
          }}
        >
          {!collapsed ? 'Fill Station' : 'FS'}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
        <div style={{ position: 'absolute', bottom: 16, width: '100%', textAlign: 'center' }}>
          <Button
            type="text"
            icon={<LogoutOutlined />}
            onClick={handleLogout}
            style={{ color: 'rgba(255,255,255,0.65)' }}
          >
            {!collapsed && t('nav.logout')}
          </Button>
        </div>
      </Sider>
      <Layout>
        <Header
          style={{
            padding: '0 16px',
            background: token.colorBgContainer,
            display: 'flex',
            alignItems: 'center',
          }}
        >
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
          />
          <span style={{ marginLeft: 'auto', color: token.colorText }}>
            {user?.name}
          </span>
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            background: token.colorBgContainer,
            borderRadius: token.borderRadius,
          }}
        >
          {children}
        </Content>
          <Footer
                  style={{
                      background: token.colorBgContainer,
                      padding: '12px 24px',
                      color: token.colorTextDescription,
                      textAlign: 'right',
                      fontSize: 12,
                  }}
          >
              {t('nav.version')}: {buildInfo.version} | {t('nav.buildDate')}: {buildInfo.buildTime}
          </Footer>
      </Layout>
    </Layout>
  );
};

export default Navigation;
