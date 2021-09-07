import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-approve-reject-item',
  templateUrl: './approve-reject-item.component.html',
  styleUrls: ['./approve-reject-item.component.css']
})
export class ApproveRejectItemComponent implements OnInit {

  public form: FormGroup;
  message: any;
    cdStatusTypes: any[]
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      itemStatus: ['', Validators.required],
      remarks: ['', Validators.required]
    })
  }

  saveRecord() {
    this.diService.sendCertificateOfInspection(this.form.value, this.data.uuid)
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.dialogRef.close(true)
              } else {
                this.message = res.message
              }
            }
        )
  }

}
