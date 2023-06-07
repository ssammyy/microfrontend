import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-generate-local-coc',
    templateUrl: './generate-local-coc.component.html',
    styleUrls: ['./generate-local-coc.component.css']
})
export class GenerateLocalCocComponent implements OnInit {

    public form: FormGroup;
    message: string;
    loading=false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            localCoi: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.loading=true
        this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "generate-coc")
            .subscribe(
                res => {
                    this.loading=false
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message,()=>{
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading=false
                }
            )
    }
}
