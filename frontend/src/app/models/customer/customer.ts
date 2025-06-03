import { Account } from "../account/account";

export interface Customer {
  customerId: number | null;
  name: string;
  email: string;
  birthday: Date;
  accounts: Account[] | null;
}
