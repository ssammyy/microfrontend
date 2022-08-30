export interface SurveillanceWorkPlanData {
  refNumber: string;
  year: string;
  dateCreated: string;
  dateCompleted: string;
  dateApproved: string;
  createdBy: string;
  status: Status;
  activities: Activity[];
}

export interface AllWorkPlanData {
  refNumber: string;
  year: string;
  dateCreated: string;
  dateCompleted: string;
  dateApproved: string;
  createdBy: string;
  status: Status;
  activities: Activity[];
}


export interface Activity {
  activityId: string;
  activityType: AcivityType;
  activityName: string;
  department: string;
  division: string;
  region: string;
  county: string;
  town: string;
  broadProductCategory: string;
  productClassification: string;
  productSubcategory: string;
  productName: string;
  resourcesRequired: ResourceRequired[];
  activityStartDate: string;
  activityEndDate: string;
  dateCreated: string;
  budget: string;
  approvedBy: string;
  dateApproved: string;
  remarks: string;
  status: Status;
}

export interface ResourceRequired {
  resourceName: string;
}

enum Status {
  Submitted= 'Submitted',
  InReview = 'In Review',
  Approved = 'Approved',
  InProgress = 'In Progress',
  Complete = 'Complete',
}

enum AcivityType {
  Surveillance= 'Surveillance',
  Complaint = 'Complaint'
}

export const SurveillanceWorkplanData: SurveillanceWorkPlanData[] = [
  {
    refNumber: 'WP#2012832318190',
    year: '2019',
    dateCreated: '23/06/2019',
    dateCompleted: '23/05/2020',
    dateApproved: '30/06/2020',
    createdBy: 'John Doe',
    status: Status.InReview,
    activities: [
      {
        activityId: 'AP#392829329U',
        activityType: AcivityType.Surveillance,
        activityName: 'Activity 1',
        department: 'Food & Agriculture',
        division: 'Food',
        region: 'Nairobi',
        county: 'Nairobi',
        town: 'Kasarani',
        broadProductCategory: 'Beverages',
        productClassification: 'Tea',
        productSubcategory: 'Flavored Tea',
        productName: 'Ketepa Flavored Tea',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: '1/07/2019',
        activityEndDate: '14/07/2019',
        dateCreated: '14/07/2019',
        budget: '120,000',
        approvedBy: 'Brian Doe',
        dateApproved: '30/06/2019',
        remarks: 'Remark',
        status: Status.Complete,
      },
      {
        activityId: 'AP#3928439329P',
        activityType: AcivityType.Complaint,
        activityName: 'string',
        department: 'string',
        division: 'string',
        region: 'string',
        county: 'string',
        town: 'string',
        broadProductCategory: 'string',
        productClassification: 'string',
        productSubcategory: 'string',
        productName: 'string',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: 'string',
        activityEndDate: 'string',
        budget: 'string',
        dateCreated: '14/07/2019',
        approvedBy: 'string',
        dateApproved: 'string',
        remarks: 'string',
        status: Status.InProgress,
      }, {
        activityId: 'AP#3928439329O',
        activityType: AcivityType.Complaint,
        activityName: 'string',
        department: 'string',
        division: 'string',
        region: 'string',
        county: 'string',
        town: 'string',
        broadProductCategory: 'string',
        productClassification: 'string',
        productSubcategory: 'string',
        productName: 'string',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: 'string',
        activityEndDate: 'string',
        budget: 'string',
        dateCreated: '14/07/2019',
        approvedBy: 'string',
        dateApproved: 'string',
        remarks: 'string',
        status: Status.InProgress,
      },
    ]
  },
];

export const sampleActivityData: Activity = {
  activityId: 'AP#392U4239',
  activityType: AcivityType.Complaint,
  activityName: 'Activity 1',
  department: 'Food & Agriculture',
  division: 'Food',
  region: 'Nairobi',
  county: 'Nairobi',
  town: 'Kasarani',
  broadProductCategory: 'Beverages',
  productClassification: 'Tea',
  productSubcategory: 'Flavored Tea',
  productName: 'Ketepa Flavored Tea',
  resourcesRequired: [
    {
      resourceName: 'car',
    },
    {
      resourceName: 'laptop'
    }
  ],
  activityStartDate: '1/07/2019',
  activityEndDate: '14/07/2019',
  budget: '120,000',
  dateCreated: '14/07/2019',
  approvedBy: 'Brian Doe',
  dateApproved: '30/06/2019',
  remarks: 'Remark',
  status: Status.Complete,
};


export const sampleWorkPlanData: AllWorkPlanData[] = [
  {
    refNumber: 'WP#2012832318190',
    year: '2019',
    dateCreated: '23/06/2019',
    dateCompleted: '23/05/2020',
    dateApproved: '30/06/2020',
    createdBy: 'John Doe',
    status: Status.InReview,
    activities: [
      {
        activityId: 'AP#392U4239',
        activityType: AcivityType.Complaint,
        activityName: 'Activity 1',
        department: 'Food & Agriculture',
        division: 'Food',
        region: 'Nairobi',
        county: 'Nairobi',
        town: 'Kasarani',
        broadProductCategory: 'Beverages',
        productClassification: 'Tea',
        productSubcategory: 'Flavored Tea',
        productName: 'Ketepa Flavored Tea',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: '1/07/2019',
        activityEndDate: '14/07/2019',
        budget: '120,000',
        dateCreated: '14/07/2019',
        approvedBy: 'Brian Doe',
        dateApproved: '30/06/2019',
        remarks: 'Remark',
        status: Status.Complete,
      },
      {
        activityId: 'AP#UW93834Y',
        activityType: AcivityType.Surveillance,
        activityName: 'string',
        department: 'string',
        division: 'string',
        region: 'string',
        county: 'string',
        town: 'string',
        broadProductCategory: 'string',
        productClassification: 'string',
        productSubcategory: 'string',
        productName: 'string',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: 'string',
        activityEndDate: 'string',
        budget: 'string',
        dateCreated: '14/07/2019',
        approvedBy: 'string',
        dateApproved: 'string',
        remarks: 'string',
        status: Status.InProgress,
      },
      {
        activityId: 'AP#UW93834Y',
        activityType: AcivityType.Surveillance,
        activityName: 'string',
        department: 'string',
        division: 'string',
        region: 'string',
        county: 'string',
        town: 'string',
        broadProductCategory: 'string',
        productClassification: 'string',
        productSubcategory: 'string',
        productName: 'string',
        resourcesRequired: [
          {
            resourceName: 'car',
          },
          {
            resourceName: 'laptop'
          }
        ],
        activityStartDate: 'string',
        activityEndDate: 'string',
        budget: 'string',
        dateCreated: '14/07/2019',
        approvedBy: 'string',
        dateApproved: 'string',
        remarks: 'string',
        status: Status.InProgress,
      },
    ]
  },
];
