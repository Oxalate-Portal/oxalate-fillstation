import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, Alert, message } from 'antd';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { forgotPassword } from '../api/authApi';

const { Title } = Typography;

const ForgotPasswordPage: React.FC = () => {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [sent, setSent] = useState(false);

  const onFinish = async (values: { email: string }) => {
    setLoading(true);
    try {
      await forgotPassword(values.email);
      setSent(true);
    } catch {
      message.error(t('common.error'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <Card style={{ width: 400 }}>
        <Title level={3} style={{ textAlign: 'center' }}>{t('auth.forgotPassword')}</Title>
        {sent ? (
          <Alert type="success" message={t('auth.passwordResetSent')} />
        ) : (
          <Form layout="vertical" onFinish={onFinish}>
            <Form.Item
              name="email"
              label={t('auth.email')}
              rules={[{ required: true }, { type: 'email' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading} block>
                {t('auth.forgotPassword')}
              </Button>
            </Form.Item>
          </Form>
        )}
        <div><Link to="/login">{t('auth.backToLogin')}</Link></div>
      </Card>
    </div>
  );
};

export default ForgotPasswordPage;
