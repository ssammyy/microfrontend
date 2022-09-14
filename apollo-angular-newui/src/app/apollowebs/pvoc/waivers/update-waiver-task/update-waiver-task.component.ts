import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-update-waiver-task',
  templateUrl: './update-waiver-task.component.html',
  styleUrls: ['./update-waiver-task.component.css']
})
export class UpdateWaiverTaskComponent implements OnInit {

  sectionOfficerStatus = [
    {
      name: 'APPROVE',
      description: 'Approve Waiver',
      section: false
    },
    {
      name: 'REJECT',
      description: 'Reject Waiver',
      section: false
    },
    {
      name: 'RECOMMEND',
      description: 'Recommend Waiver',
      section: false
    },
    {
      name: 'DEFER',
      description: 'Defer Waiver Request',
      section: true
    }
  ]
  complaintStatus: any[]
  recommendations: any[]
  form: FormGroup
  errors: any
  loading: boolean = false

  constructor(private pvocService: PVOCService, private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,) {
  }

  ngOnInit(): void {
    // Status
    this.complaintStatus = this.sectionOfficerStatus
    // this.data.is_pvoc_officer = false
    // for (let d of this.sectionOfficerStatus) {
    //   if (this.data.is_pvoc_officer) {
    //     if (d.section) {
    //       this.complaintStatus.push(d)
    //     }
    //   } else {
    //     if (!d.section) {
    //       this.complaintStatus.push(d)
    //     }
    //   }
    // }
    // Form
    this.form = this.fb.group({
      taskId: [this.data.taskId],
      action: ["0", this.data.is_pvoc_officer ? [Validators.required] : []],
      status: [null, [Validators.required]],
      remarks: [null, Validators.required]
    })
    // Complaint Recommendations
    this.loadData()
  }

  loadData() {
    this.pvocService.getComplaintRecommendations()
        .subscribe(
            res => {
              if (res.responseCode === '00') {
                this.recommendations = res.data
              }
            }
        )
  }

  saveRecord() {
    this.loading = true
    let pData = this.form.value
    // Convert recommendation to actionId
    pData.action = parseInt(this.form.value.action)
    this.pvocService.updateWaiverStatus(this.data.waiverId, pData)
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.pvocService.showSuccess(res.message, () => {
                  this.dialogRef.close(true)
                })

              } else {
                this.errors = res.errors
                this.pvocService.showError(res.message)
              }
              this.loading = false
            }
        )
  }

}
