import React, {useState} from 'react';
import {Alert, Button, Card, Form, Input, message, Typography} from 'antd';
import {Link, useSearchParams} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {resetPassword} from '../api/authApi';

const { Title } = Typography;

const ResetPasswordPage: React.FC = () => {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token') ?? '';
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const onFinish = async (values: { newPassword: string }) => {
    setLoading(true);
    try {
      await resetPassword(token, values.newPassword);
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
        <Title level={3} style={{ textAlign: 'center' }}>{t('auth.resetPassword')}</Title>
        {success ? (
          <Alert
            type="success"
            title={t('auth.passwordResetSuccess')}
            action={<Link to="/login">{t('auth.backToLogin')}</Link>}
          />
        ) : (
          <Form layout="vertical" onFinish={onFinish}>
            <Form.Item
              name="newPassword"
              label={t('auth.newPassword')}
              rules={[{ required: true }, { min: 8 }]}
            >
              <Input.Password />
            </Form.Item>
            <Form.Item
              name="confirmPassword"
              label={t('auth.confirmPassword')}
              dependencies={['newPassword']}
              rules={[
                { required: true },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue('newPassword') === value) {
                      return Promise.resolve();
                    }
                    return Promise.reject(new Error('Passwords do not match'));
                  },
                }),
              ]}
            >
              <Input.Password />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading} block>
                {t('auth.resetPassword')}
              </Button>
            </Form.Item>
          </Form>
        )}
      </Card>
    </div>
  );
};

export default ResetPasswordPage;
