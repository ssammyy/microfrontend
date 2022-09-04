export interface TaskData {
  tcSecretaryId?: number;
  nepOfficerId?: number;
  NotificationDueIndex: string;
  NotificationCategory: string;
}

export interface NepNotification {
  taskId: string;
  name: string;
  taskData: TaskData;
}

export interface DraftNotification {
  tcNotificationID: number;
  nepOfficerID: number;
  notificationDocumentIndex: string;
  taskID: string;
}

export interface NotificationData{
  tcNotificationID: number;
  tcSecretaryId: number;
  nepOfficerId: number;
  NotificationDueIndex: string;
  notificationDocumentIndex: string;
  NotificationCategory: string;
}

export interface InboundNotification{
  taskId: string;
  name: string;
  taskData: NotificationData;
}

export interface finalSubmit{
  taskId: string;
}
