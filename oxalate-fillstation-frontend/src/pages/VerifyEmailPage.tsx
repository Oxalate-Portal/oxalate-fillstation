import React, { useEffect, useState } from 'react';
import { Card, Typography, Spin, Alert } from 'antd';
import { Link, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { verifyEmail } from '../api/authApi';

const { Title } = Typography;

const VerifyEmailPage: React.FC = () => {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token') ?? '';
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');

  useEffect(() => {
    verifyEmail(token)
      .then(() => setStatus('success'))
      .catch(() => setStatus('error'));
  }, [token]);

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <Card style={{ width: 400 }}>
        <Title level={3} style={{ textAlign: 'center' }}>{t('auth.verifyEmail')}</Title>
        {status === 'loading' && <Spin />}
        {status === 'success' && (
          <Alert type="success" message={t('auth.loginSuccess')} action={<Link to="/login">{t('auth.login')}</Link>} />
        )}
        {status === 'error' && <Alert type="error" message={t('common.error')} />}
      </Card>
    </div>
  );
};

export default VerifyEmailPage;
