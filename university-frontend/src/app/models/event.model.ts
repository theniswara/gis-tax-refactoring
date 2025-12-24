export interface Event {
  id: number;
  title: string;
  date: string;
  time: string;
  location: string;
  type: string;
  description: string;
  image: string;
  registrations: number;
  capacity: number;
  speakers: string[];
}