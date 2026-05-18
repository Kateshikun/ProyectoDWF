import { type ReactNode } from 'react';

interface CardProps {
  title?: string;
  children: ReactNode;
  action?: ReactNode;
  className?: string;
  padding?: boolean;
}

export default function Card({ title, children, action, className = '', padding = true }: CardProps) {
  return (
    <div className={`bg-white rounded-xl border border-slate-200 shadow-sm ${className}`}>
      {title && (
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-200">
          <h3 className="text-base font-semibold text-slate-900">{title}</h3>
          {action}
        </div>
      )}
      <div className={padding ? 'p-6' : ''}>{children}</div>
    </div>
  );
}
