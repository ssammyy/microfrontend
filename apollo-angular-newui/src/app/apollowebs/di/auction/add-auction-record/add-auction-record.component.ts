import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {AddEditAuctionItemComponent} from "../add-edit-auction-item/add-edit-auction-item.component";
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-add-auction-record',
    templateUrl: './add-auction-record.component.html',
    styleUrls: ['./add-auction-record.component.css']
})
export class AddAuctionRecordComponent implements OnInit {
    message: String
    form: FormGroup
    cfsStation: any
    loading = false
    categories: any
    auctionItems: LocalDataSource = new LocalDataSource([])
    auctionItemsSetting = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'deleteItem', title: '<i class="fa fa-trash-alt">Delete</i>'},
                {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            serialNo: {
                title: 'Serial No.',
                type: 'string'
            },
            chassisNo: {
                title: 'Chassis No.',
                type: 'string'
            },
            itemName: {
                title: 'Item Name',
                type: 'string'
            },
            itemType: {
                title: 'Item Type',
                type: 'string'
            },
            quantity: {
                title: 'Quantity',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private fb: FormBuilder, private dialog: MatDialog, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        console.log("Add auction Items: 2")
        this.form = this.fb.group({
            categoryCode: [null, Validators.required],
            cfsCode: [null, Validators.required],
            auctionLotNo: [null, Validators.required],
            containerNo: [null, Validators.required],
            manifestNo: [null, Validators.required],
            auctionDate: [null, Validators.required],
            shipmentPort: [null, Validators.required],
            shipmentDate: [null, Validators.required],
            arrivalDate: [null, Validators.required],
            importerName: [null, Validators.required],
            importerPhone: [null],
            itemLocation: [null, Validators.required],
            containerSize: [null]
        })
        this.categories = this.data.categories
        this.form.valueChanges
            .subscribe(
                res => {
                    console.log(res)
                }
            )
        this.loadMyCfs()
    }

    loadMyCfs() {
        this.diService.listMyCfs()
            .subscribe(res => {
                if (res.responseCode === '00') {
                    this.cfsStation = res.data
                }
            })
    }

    isValid(): Boolean {
        return this.auctionItems.count() > 0 && this.form.valid
    }

    addItem(oldItem: any) {
        this.dialog.open(AddEditAuctionItemComponent, {
            data: {
                old: oldItem,
                categoryCode: this.form.value.categoryCode
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        console.log(res)
                        this.auctionItems.prepend(res)
                            .then(() => {

                            })
                    }
                }
            )
    }

    deleteItem(item: any) {
        this.auctionItems.remove(item)
            .then(
                () => {
                    console.log("Removed")
                }
            )
    }

    auctionItemActions(event) {
        switch (event.action) {
            case "editRecord":
                this.addItem(event.data)
                break
            case "deleteItem":
                this.deleteItem(event.data)
                break
        }
    }

    saveAuctionDetails() {
        this.loading = true
        let data = this.form.value
        this.auctionItems.getAll().then(dd => {
            data['items'] = dd
            data["auctionDate"] = new DatePipe("En-US").transform(this.form.value.auctionDate, "dd-MM-yyyy")
            data["shipmentDate"] = new DatePipe("En-US").transform(this.form.value.shipmentDate, "dd-MM-yyyy")
            data["arrivalDate"] = new DatePipe("En-US").transform(this.form.value.arrivalDate, "dd-MM-yyyy")
            this.diService.addAuctionItem(data)
                .subscribe(
                    res => {
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
                        this.message = error.message
                    }
                )
        })

    }
}
