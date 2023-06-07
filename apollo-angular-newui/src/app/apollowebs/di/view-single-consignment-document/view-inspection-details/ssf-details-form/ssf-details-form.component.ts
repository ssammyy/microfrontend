import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AddLaboratoryRequestComponent} from "./add-laboratory-request/add-laboratory-request.component";
import {MatTableDataSource} from "@angular/material/table";

@Component({
    selector: 'app-ssf-details-form',
    templateUrl: './ssf-details-form.component.html',
    styleUrls: ['./ssf-details-form.component.css']
})
export class SsfDetailsFormComponent implements OnInit {
    public displayedColumns = ['count', 'laboratoryName', 'testParameters', 'remarks', 'actions']
    message: any
    loading = false
    form: FormGroup
    laboratories: any
    laboratoryRequests: MatTableDataSource<any>

    constructor(private dialog: MatDialog, @Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>, private fb: FormBuilder, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            permitNumber: ['DI',],
            ssfSubmissionDate: ['', Validators.required],
            returnOrDispose: ['', Validators.required],
            conditionOfSample: ['', [Validators.required, Validators.maxLength(100)]],
            description: ['', Validators.maxLength(256)],
        })
        this.laboratoryRequests = new MatTableDataSource<any>([]);
        // Load labs
        this.loadLaboratories()

    }

    addLaboratoryRequest(data?: any, loc?: any) {
        this.dialog.open(AddLaboratoryRequestComponent, {
            data: {
                laboratories: this.laboratories,
                request: data
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        console.log(res)
                        if (loc && data) {
                            this.laboratoryRequests.data.splice(loc, 1, res) // Delete and replace
                        } else {
                            this.laboratoryRequests.data.push(res)
                        }
                        this.laboratoryRequests.data = this.laboratoryRequests.data
                    }
                }
            )
    }

    removeRequest(request: any, loc: any) {
        console.log('Delete at: ' + loc)
        this.laboratoryRequests.data.splice(loc, 1)
        this.laboratoryRequests.data = this.laboratoryRequests.data
    }

    customAction(request, action, loc) {
        switch (action) {
            case 'edit':
                this.addLaboratoryRequest(request)
                break
            case 'delete':
                this.removeRequest(request, loc)
                break
            default:
                console.log("Unknown action: " + action)
        }
    }

    loadLaboratories() {
        this.loading = true
        this.diService.loadLaboratories()
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode == '00') {
                        this.laboratories = res.data
                    }
                },
                error => {
                    this.loading = false
                }
            )
    }

    saveSsfRecord() {
        if (this.laboratoryRequests.data.length == 0) {
            this.diService.showError("Please select at least one laboratory request", () => {
                this.message = "Please, select laboratory"
            })
        } else {
            this.loading = true
            this.message = null
            let data = this.form.value
            data['labRequests'] = this.laboratoryRequests.data
            this.diService.saveSSFDetails(data, this.data.uuid)
                .subscribe(
                    res => {
                        this.loading = false
                        if (res.responseCode == "00") {
                            this.diService.showSuccess(res.message, () => {
                                this.dialogRef.close(true)
                            })

                        } else {
                            this.message = res.message
                        }
                    },
                    error => {
                        this.loading = false
                    }
                )
        }
    }
}
