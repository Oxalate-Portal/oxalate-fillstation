import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, Select, Alert, message } from 'antd';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';

const { Title } = Typography;

const languageOptions = [
  { value: 'de', label: 'Deutsch (de)' },
  { value: 'en', label: 'English (en)' },
  { value: 'es', label: 'Español (es)' },
  { value: 'fi', label: 'Suomi (fi)' },
  { value: 'sv', label: 'Svenska (sv)' },
];

const RegisterPage: React.FC = () => {
  const { t } = useTranslation();
  const { register } = useAuth();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const onFinish = async (values: { name: string; email: string; password: string; language: string }) => {
    setLoading(true);
    try {
      await register(values);
      setSuccess(true);
    } catch {
      message.error(t('common.error'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <Card style={{ width: 400 }}>
        <Title level={3} style={{ textAlign: 'center' }}>{t('auth.register')}</Title>
        {success ? (
          <Alert
            type="success"
            message={t('auth.registerSuccess')}
            action={<Link to="/login">{t('auth.backToLogin')}</Link>}
          />
        ) : (
          <>
            <Form layout="vertical" onFinish={onFinish}>
              <Form.Item
                name="name"
                label={t('auth.name')}
                rules={[{ required: true }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="email"
                label={t('auth.email')}
                rules={[{ required: true }, { type: 'email' }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="password"
                label={t('auth.password')}
                rules={[{ required: true }, { min: 8 }]}
              >
                <Input.Password />
              </Form.Item>
              <Form.Item
                name="language"
                label={t('auth.language')}
                rules={[{ required: true }]}
                initialValue="en"
              >
                <Select options={languageOptions} />
              </Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit" loading={loading} block>
                  {t('auth.register')}
                </Button>
              </Form.Item>
            </Form>
            <div>{t('auth.haveAccount')} <Link to="/login">{t('auth.login')}</Link></div>
          </>
        )}
      </Card>
    </div>
  );
};

export default RegisterPage;
