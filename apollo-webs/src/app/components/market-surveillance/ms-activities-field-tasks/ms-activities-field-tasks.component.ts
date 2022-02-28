import {Component, OnInit} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {Location} from '@angular/common';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-ms-activities-field-tasks',
  templateUrl: './ms-activities-field-tasks.component.html',
  styleUrls: ['./ms-activities-field-tasks.component.css']
})
export class MsActivitiesFieldTasksComponent implements OnInit {

  arrowLeftIcon = faArrowLeft;
  deleteIcon = faTrash;
  public seizureFormGroup!: FormGroup;
  public sampleCollectionFormGroup!: FormGroup;
  public sampleSubmissionFormGroup!: FormGroup;


  constructor(private location: Location,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.seizureFormGroup = this.formBuilder.group({
      serialNumber: ['', Validators.required],
      toBeSentTo: ['', Validators.required],
      premises: ['', Validators.required],
      standard: ['', Validators.required],
      manufacturerTraderName: ['', Validators.required],
      postalAddress: ['', Validators.required],
      physicalLocation: ['', Validators.required],
      quantity: ['', Validators.required],
      goodsDescription: ['', Validators.required],
      inspectorName: ['', Validators.required],
      inspectorDesignation: ['', Validators.required],
      manufacturerTraderDesignation: ['', Validators.required],
      witnessName: ['', Validators.required],
      witnessDesignation: ['', Validators.required],
      declarationDate: ['', Validators.required],
      personGoodsSeized: ['', Validators.required],
      residence: ['', Validators.required],
      employmentDesignation: ['', Validators.required],
      businessLocation: ['', Validators.required],
      declarationStatement: ['', Validators.required],
      witnessIdNo: ['', Validators.required],
    });
    this.sampleCollectionFormGroup = this.formBuilder.group({
      manufacturerTraderName: ['', Validators.required],
      manufacturerTraderAddress: ['', Validators.required],
      productName: ['', Validators.required],
      brandName: ['', Validators.required],
      batchNumber: ['', Validators.required],
      batchSize: ['', Validators.required],
      sampleSize: ['', Validators.required],
      samplingMethod: ['', Validators.required],
      reasonsForCollectingSamples: ['', Validators.required],
      remarks: ['', Validators.required],
      officerName: ['', Validators.required],
      officerDesignation: ['', Validators.required],
      dateSignedByOfficer: ['', Validators.required],
      witnessName: ['', Validators.required],
      witnessDesignation: ['', Validators.required],
      dateSignedByWitness: ['', Validators.required],
    });
    this.sampleSubmissionFormGroup = this.formBuilder.group({
      productName: ['', Validators.required],
      sizeOfTestSample: ['', Validators.required],
      sizeOfRefSample: ['', Validators.required],
      fileRefNumber: ['', Validators.required],
      packaging: ['', Validators.required],
      labelling: ['', Validators.required],
      condition: ['', Validators.required],
      referenceStandards: ['', Validators.required],
      parameter: ['', Validators.required],
      scfNumber: ['', Validators.required],
      senderName: ['', Validators.required],
      senderDesignation: ['', Validators.required],
      senderAddress: ['', Validators.required],
      senderDate: ['', Validators.required],
      receiverName: ['', Validators.required],
      sampleReference: ['', Validators.required],
      testCharges: ['', Validators.required],
      receiptNumber: ['', Validators.required],
      invoiceNumber: ['', Validators.required],
      lab: ['', Validators.required],
      disposal: ['', Validators.required],
      remarks: ['', Validators.required],
    });
  }


  public backClicked(): void {
    this.location.back();
  }

}
