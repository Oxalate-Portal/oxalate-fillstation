import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Space, Typography, Popconfirm, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import type { ColumnsType } from 'antd/es/table';
import { getCylinders, createCylinder, updateCylinder, deleteCylinder } from '../api/cylinderApi';
import type { Cylinder } from '../types';

const { Title } = Typography;

const CylindersPage: React.FC = () => {
  const { t } = useTranslation();
  const [cylinders, setCylinders] = useState<Cylinder[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingCylinder, setEditingCylinder] = useState<Cylinder | null>(null);
  const [form] = Form.useForm();
  const [submitLoading, setSubmitLoading] = useState(false);

  const fetchCylinders = () => {
    setLoading(true);
    getCylinders()
      .then((res) => setCylinders(res.data as Cylinder[]))
      .catch(() => message.error(t('common.error')))
      .finally(() => setLoading(false));
  };

  useEffect(fetchCylinders, []);

  const handleEdit = (record: Cylinder) => {
    setEditingCylinder(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteCylinder(id);
      message.success(t('common.success'));
      fetchCylinders();
    } catch {
      message.error(t('common.error'));
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitLoading(true);
      if (editingCylinder) {
        await updateCylinder(editingCylinder.id, values as Partial<Omit<Cylinder, 'id' | 'userId'>>);
      } else {
        await createCylinder(values as Omit<Cylinder, 'id' | 'userId'>);
      }
      message.success(t('common.success'));
      setModalOpen(false);
      fetchCylinders();
    } catch {
      message.error(t('common.error'));
    } finally {
      setSubmitLoading(false);
    }
  };

  const columns: ColumnsType<Cylinder> = [
    { title: t('common.id'), dataIndex: 'id', key: 'id', width: 60 },
    { title: t('cylinders.name'), dataIndex: 'name', key: 'name' },
    { title: t('cylinders.volume'), dataIndex: 'volume', key: 'volume' },
    { title: t('cylinders.workingPressure'), dataIndex: 'workingPressure', key: 'workingPressure' },
    { title: t('cylinders.serialNumber'), dataIndex: 'serialNumber', key: 'serialNumber' },
    {
      title: t('common.actions'),
      key: 'actions',
      render: (_, record) => (
        <Space>
          <Button size="small" onClick={() => handleEdit(record)}>{t('common.edit')}</Button>
          <Popconfirm title={t('cylinders.confirmDelete')} onConfirm={() => handleDelete(record.id)} okText={t('common.yes')} cancelText={t('common.no')}>
            <Button size="small" danger>{t('common.delete')}</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <Space direction="vertical" style={{ width: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={2}>{t('cylinders.title')}</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingCylinder(null); form.resetFields(); setModalOpen(true); }}>{t('cylinders.add')}</Button>
      </div>
      <Table dataSource={cylinders} columns={columns} rowKey="id" loading={loading} />
      <Modal title={editingCylinder ? t('cylinders.edit') : t('cylinders.add')} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitLoading} okText={t('common.save')} cancelText={t('common.cancel')}>
        <Form form={form} layout="vertical">
          <Form.Item name="name" label={t('cylinders.name')} rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="volume" label={t('cylinders.volume')} rules={[{ required: true }]}><InputNumber style={{ width: '100%' }} min={0} /></Form.Item>
          <Form.Item name="workingPressure" label={t('cylinders.workingPressure')} rules={[{ required: true }]}><InputNumber style={{ width: '100%' }} min={0} /></Form.Item>
          <Form.Item name="serialNumber" label={t('cylinders.serialNumber')} rules={[{ required: true }]}><Input /></Form.Item>
        </Form>
      </Modal>
    </Space>
  );
};

export default CylindersPage;
