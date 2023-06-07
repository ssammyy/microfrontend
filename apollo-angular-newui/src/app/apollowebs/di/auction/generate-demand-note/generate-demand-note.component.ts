import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-generate-demand-note',
  templateUrl: './generate-demand-note.component.html',
  styleUrls: ['./generate-demand-note.component.css']
})
export class GenerateDemandNoteComponent implements OnInit {

  public form: FormGroup;
  public fees: any = [];
  loading: boolean = false
  message: string;
  sections: any

  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.sections = {}
    this.loadFees()
    this.form = this.fb.group({
      feeId: ['', Validators.required],
      amount: ['0.0'],
      remarks: ['', Validators.required]
    })
  }

  loadFees() {
    this.diService.demandNoteFees()
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.fees = res.data
              } else {
                console.log(res.message)
              }
            }
        )

  }

  saveRecord() {
    this.loading = true
      let data=this.form.value
      data['feeId']=parseInt(this.form.value.feeId)
    this.diService.requestAuctionPayment(this.form.value, this.data.requestId)
        .subscribe(res => {
              this.loading = false
              if (res.responseCode === "00") {
                this.diService.showSuccess(res.message, () => {
                  this.dialogRef.close(true)
                })
              } else {
                this.message = res.message
              }
            },
            error => {
              this.loading = false
            })
  }
}
