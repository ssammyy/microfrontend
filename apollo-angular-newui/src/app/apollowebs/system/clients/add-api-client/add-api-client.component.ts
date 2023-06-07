import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SystemService} from "../../../../core/store/data/system/system.service";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";

@Component({
    selector: 'app-add-api-client',
    templateUrl: './add-api-client.component.html',
    styleUrls: ['./add-api-client.component.css']
})
export class AddApiClientComponent implements OnInit {
    form: FormGroup
    loading: Boolean = false
    clientRoles = [
        {
            name: "PVOC",
            description: "PVOC Role"
        },
        {
            name: "KIMS",
            description: "KIMS roles"
        },
        {
            name: "PAYMENT",
            description: "SAGE callback role"
        }
    ]
    clientTypes = [
        {
            name: "PVOC",
            description: "PVOC partner"
        },
        {
            name: "INTERNAL",
            description: "Internal client"
        },
        {
            name: "PAYMENT",
            description: "Callback client"
        }
    ]

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,
                private systemService: SystemService, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        console.log(this.data);
        this.form = this.fb.group({
            clientType: [null, [Validators.required]],
            clientName: [null, [Validators.required]],
            clientRole: [null, [Validators.required]],
            callbackURL: [null, [Validators.required]],
            eventsURL: [null, [Validators.required]],
            descriptions: [null, [Validators.required]]
        })
    }

    addApiClient() {
        if (this.data) {
            if (this.data.partnerId) {
                this.creatPartnerClient()
            } else {
                this.createUpdateApiClient()
            }
        } else {
            this.createUpdateApiClient()
        }
    }

    createUpdateApiClient() {
        let result = this.systemService.addApiClient(this.form.value)
        if (this.data) {
            result = this.systemService.updateApiClient(this.form.value, this.data.clientId)
        }
        result
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.pvocService.showSuccess(res.message, () => {
                            this.dialogRef.close(res.data)
                        })
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError(error.message)
                }
            )
    }

    creatPartnerClient() {
        this.pvocService.addPartnerApiClient(this.form.value, this.data.partnerId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.pvocService.showSuccess(res.message + " \nClient ID is " + res.data.client_id + " and the new secret is " + res.data.client_secret, () => {
                            this.dialogRef.close(res.data)
                        })
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError(error.message)
                }
            )
    }

}
