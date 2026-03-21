import React, {useCallback, useEffect, useState} from 'react';
import {Button, Form, Input, message, Modal, Popconfirm, Space, Table, Typography} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import {useTranslation} from 'react-i18next';
import type {ColumnsType} from 'antd/es/table';
import {createConfig, deleteConfig, getConfig, updateConfig} from '../api/adminApi';
import type {Configuration} from '../types';

const { Title } = Typography;

const AdminPage: React.FC = () => {
  const { t } = useTranslation();
  const [configs, setConfigs] = useState<Configuration[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingConfig, setEditingConfig] = useState<Configuration | null>(null);
  const [form] = Form.useForm();
  const [submitLoading, setSubmitLoading] = useState(false);

    const fetchConfigs = useCallback(() => {
        setLoading(true);
        getConfig().then((r) => setConfigs(r.data as Configuration[])).catch(() => message.error(t('common.error'))).finally(() => setLoading(false));
    }, [t]);
    useEffect(() => {
        fetchConfigs();
    }, [fetchConfigs]);

  const handleEdit = (record: Configuration) => { setEditingConfig(record); form.setFieldsValue(record); setModalOpen(true); };
  const handleDelete = async (id: number) => { try { await deleteConfig(id); message.success(t('common.success')); fetchConfigs(); } catch { message.error(t('common.error')); } };
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitLoading(true);
      if (editingConfig) { await updateConfig(editingConfig.id, values as Partial<Omit<Configuration, 'id'>>); } else { await createConfig(values as Omit<Configuration, 'id'>); }
      message.success(t('common.success')); setModalOpen(false); fetchConfigs();
    } catch { message.error(t('common.error')); } finally { setSubmitLoading(false); }
  };

  const columns: ColumnsType<Configuration> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('admin.group'), dataIndex: 'groupName', key: 'groupName' },
    { title: t('admin.key'), dataIndex: 'configKey', key: 'configKey' },
    { title: t('admin.value'), dataIndex: 'configValue', key: 'configValue' },
    { title: t('common.actions'), key: 'actions', render: (_, record) => (
      <Space>
        <Button size="small" onClick={() => handleEdit(record)}>{t('common.edit')}</Button>
        <Popconfirm title={t('admin.confirmDelete')} onConfirm={() => handleDelete(record.id)} okText={t('common.yes')} cancelText={t('common.no')}>
          <Button size="small" danger>{t('common.delete')}</Button>
        </Popconfirm>
      </Space>
    )},
  ];

  return (
    <Space direction="vertical" style={{ width: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={2}>{t('admin.title')}</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingConfig(null); form.resetFields(); setModalOpen(true); }}>{t('admin.add')}</Button>
      </div>
      <Table dataSource={configs} columns={columns} rowKey="id" loading={loading} />
      <Modal title={editingConfig ? t('admin.edit') : t('admin.add')} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitLoading} okText={t('common.save')} cancelText={t('common.cancel')}>
        <Form form={form} layout="vertical">
          <Form.Item name="groupName" label={t('admin.group')} rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="configKey" label={t('admin.key')} rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="configValue" label={t('admin.value')} rules={[{ required: true }]}><Input /></Form.Item>
        </Form>
      </Modal>
    </Space>
  );
};

export default AdminPage;
